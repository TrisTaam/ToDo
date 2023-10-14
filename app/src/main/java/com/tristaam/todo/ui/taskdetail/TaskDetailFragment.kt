package com.tristaam.todo.ui.taskdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.adapter.subtask.ISubtaskListener
import com.tristaam.todo.adapter.subtask.SubtaskAdapter
import com.tristaam.todo.databinding.FragmentTaskDetailBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Subtask
import com.tristaam.todo.ui.dialog.selectPriority.ISelectPriorityListener
import com.tristaam.todo.ui.dialog.selectPriority.SelectPriorityDialogFragment
import com.tristaam.todo.ui.dialog.selectProject.ISelectProjectListener
import com.tristaam.todo.ui.dialog.selectProject.SelectProjectDialogFragment
import com.tristaam.todo.utils.Alarm
import com.tristaam.todo.utils.DateTimeUtils
import com.tristaam.todo.utils.DateTimeUtils.getHour
import com.tristaam.todo.utils.DateTimeUtils.getMinute
import com.tristaam.todo.utils.DateTimeUtils.minus
import com.tristaam.todo.utils.datePicker.DatePickerDialog
import com.tristaam.todo.utils.datePicker.IDatePickerListener
import com.tristaam.todo.utils.observeOnce
import com.tristaam.todo.utils.timePicker.ITimePickerListener
import com.tristaam.todo.utils.timePicker.TimePickerDialog
import java.text.ParseException
import java.util.Date

class TaskDetailFragment : Fragment() {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskDetailViewModel by viewModels {
        TaskDetailViewModel.TaskDetailViewModelFactory(requireContext())
    }
    private val subtaskAdapter: SubtaskAdapter by lazy {
        SubtaskAdapter(object : ISubtaskListener {
            override fun onTick(position: Int) {
                viewModel.updateSubtaskStatus(position)
                subtaskAdapter.setData(viewModel.subtasks)
                subtaskAdapter.notifyItemChanged(position)
            }

            override fun onDelete(position: Int) {
                viewModel.deleteSubtask(position)
                subtaskAdapter.setData(viewModel.subtasks)
                subtaskAdapter.notifyItemRemoved(position)
            }

            override fun onSave(position: Int, newName: String) {
                if (position == -1) {
                    return
                }
                viewModel.updateSubtaskName(position, newName)
                subtaskAdapter.setData(viewModel.subtasks)
                subtaskAdapter.notifyItemChanged(position)
            }
        })
    }

    companion object {
        const val TASK_ID = "taskId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.GONE
        val taskId = arguments?.getInt(TASK_ID) ?: 0
        binding.apply {
            toolbar.setNavigationOnClickListener { onBackPressed() }
            toolbar.menu.findItem(R.id.action_delete).setOnMenuItemClickListener {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setTitle(getString(R.string.delete))
                    setMessage(getString(R.string.delete_task_alert))
                    setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteTask(viewModel.task)
                        findNavController().popBackStack()
                    }
                    setNegativeButton("No") { _, _ -> }
                    show()
                }
                true
            }
            chipDate.setOnClickListener { onChipDateClick() }
            chipTime.setOnClickListener { onChipTimeClick() }
            clPriority.setOnClickListener { onClPriorityClick() }
            clProject.setOnClickListener { onClProjectClick() }
            clAddSubtask.setOnClickListener { onClAddSubtaskClick() }
            clReminder.setOnClickListener { onClReminderClick() }
            rvSubtasks.adapter = subtaskAdapter
            rvSubtasks.layoutManager = LinearLayoutManager(context)
            viewModel.getTaskById(taskId).observe(viewLifecycleOwner) {
                viewModel.task = it
                cbTask.isChecked = it.status
                etTaskTitle.setText(it.title)
                etDescription.setText(it.description)
                viewModel.setPriority(it.priority)
                viewModel.getProject(it.projectId).observe(viewLifecycleOwner) { project ->
                    viewModel.projectId = project.id
                    tvProject.text = project.name
                }
                viewModel.setRemindBefore(it.dueDate.minus(it.remindAt))
                chipDate.text = DateTimeUtils.dateFormat.format(it.dueDate)
                chipTime.text = DateTimeUtils.timeFormat.format(it.dueDate)
                viewModel.getSubtasksByTaskId(taskId).observe(viewLifecycleOwner) { subtasks ->
                    viewModel.setSubtasks(subtasks)
                    subtaskAdapter.setData(viewModel.subtasks)
                    subtaskAdapter.notifyDataSetChanged()
                }
                viewModel.remindBefore.observe(viewLifecycleOwner) { remindBefore ->
                    tvReminder.text =
                        getString(
                            R.string.reminder,
                            remindBefore.getHour(),
                            remindBefore.getMinute()
                        )
                }
            }
            initPriorityViewGroup()
        }
    }

    private fun onClReminderClick() {
        TimePickerDialog.show(
            childFragmentManager,
            "Time picker",
            false,
            object : ITimePickerListener {
                override fun onTimeSelected(hour: Int, minute: Int) {
                    try {
                        viewModel.setRemindBefore(
                            DateTimeUtils.timeFormat.parse(
                                "%02d:%02d".format(
                                    hour,
                                    minute
                                )
                            )!!
                        )
                    } catch (e: ParseException) {
                        Log.e("DetailTaskFragment", "onTimeSelected: ${e.message}")
                    }
                }
            })
    }

    private fun initPriorityViewGroup() {
        binding.apply {
            viewModel.priority.observe(viewLifecycleOwner) {
                tvPriority.text = getString(R.string.priority_selected, it.priorityName)
                when (it) {
                    Priority.URGENT -> {
                        ivPriority.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_flag_red
                            )
                        )
                        tvPriority.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                    }

                    Priority.HIGH -> {
                        ivPriority.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_flag_orange
                            )
                        )
                        tvPriority.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.orange
                            )
                        )
                    }

                    Priority.MEDIUM -> {
                        ivPriority.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_flag_yellow
                            )
                        )
                        tvPriority.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.yellow
                            )
                        )
                    }

                    Priority.LOW -> {
                        ivPriority.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_flag_green
                            )
                        )
                        tvPriority.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun onClAddSubtaskClick() {
        binding.apply {
            viewModel.addSubtask(
                Subtask(
                    name = "",
                    taskId = viewModel.task.id,
                    status = false
                )
            )
            subtaskAdapter.setData(viewModel.subtasks)
            subtaskAdapter.notifyItemInserted(viewModel.subtasks.size - 1)
        }
    }

    private fun onBackPressed() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.save))
            setMessage(getString(R.string.save_task_alert))
            setPositiveButton("Yes") { _, _ ->
                saveTask()
            }
            setNegativeButton("No") { _, _ ->
                findNavController().popBackStack()
            }
            show()
        }
    }

    private fun onClProjectClick() {
        SelectProjectDialogFragment(object : ISelectProjectListener {
            override fun onSelectProject(projectId: Int) {
                binding.apply {
                    viewModel.projectId = projectId
                    viewModel.getProject(projectId).observe(viewLifecycleOwner) {
                        tvProject.text = it.name
                    }
                }
            }
        }).show(childFragmentManager, "Select project")
    }

    private fun onClPriorityClick() {
        SelectPriorityDialogFragment(object : ISelectPriorityListener {
            override fun onSelectPriority(priority: Priority) {
                viewModel.setPriority(priority)
            }
        }).show(childFragmentManager, "Select priority")
    }

    private fun onChipDateClick() {
        DatePickerDialog.show(childFragmentManager, "Date picker", object : IDatePickerListener {
            override fun onDateSelected(day: Int, month: Int, year: Int) {
                binding.chipDate.text = getString(R.string.date_format, day, month, year)
            }
        })
    }

    private fun onChipTimeClick() {
        TimePickerDialog.show(
            childFragmentManager,
            "Time picker",
            true,
            object : ITimePickerListener {
                override fun onTimeSelected(hour: Int, minute: Int) {
                    binding.chipTime.text = getString(R.string.time_format, hour, minute)
                }
            })
    }

    private fun saveTask() {
        try {
            binding.apply {
                if (etTaskTitle.text.toString().isEmpty() ||
                    etDescription.text.toString().isEmpty()
                ) {
                    throw Exception()
                } else {
                    val dueDateTmp =
                        DateTimeUtils.dateTimeFormat.parse("${chipDate.text} ${chipTime.text}")!!
                    var remindAtTmp = Date()
                    viewModel.remindBefore.observeOnce(viewLifecycleOwner) {
                        remindAtTmp = dueDateTmp.minus(it)
                    }
                    viewModel.task.apply {
                        title = etTaskTitle.text.toString()
                        description = etDescription.text.toString()
                        dueDate = dueDateTmp
                        remindAt = remindAtTmp
                        status = cbTask.isChecked
                        priority = viewModel.priority.value!!
                        projectId = viewModel.projectId
                        viewModel.updateTask(this)
                        cancelAlarm(id * 2, title, "Your task is due date")
                        cancelAlarm(
                            id * 2 + 1,
                            title,
                            "Your task is ${tvReminder.text} due date"
                        )
                        createAlarm(id * 2, title, "Your task is due date", dueDate)
                        createAlarm(
                            id * 2 + 1,
                            title,
                            "Your task is ${binding.tvReminder.text} due date",
                            remindAt
                        )
                    }
                }
            }
            findNavController().popBackStack()
        } catch (e: Exception) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAlarm(id: Int, title: String, message: String, date: Date) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        Alarm.createAlarm(requireContext(), bundle, id, date)
    }

    private fun cancelAlarm(id: Int, title: String, message: String) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        Alarm.cancelAlarm(requireContext(), bundle, id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }
}