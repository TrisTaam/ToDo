package com.tristaam.todo.ui.createTask

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
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.adapter.subtask.ISubtaskListener
import com.tristaam.todo.adapter.subtask.SubtaskAdapter
import com.tristaam.todo.databinding.FragmentCreateTaskBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Subtask
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.dialog.selectPriority.ISelectPriorityListener
import com.tristaam.todo.ui.dialog.selectPriority.SelectPriorityDialogFragment
import com.tristaam.todo.ui.dialog.selectProject.ISelectProjectListener
import com.tristaam.todo.ui.dialog.selectProject.SelectProjectDialogFragment
import com.tristaam.todo.utils.DateTimeUtils
import com.tristaam.todo.utils.datePicker.DatePickerDialog
import com.tristaam.todo.utils.datePicker.IDatePickerListener
import com.tristaam.todo.utils.timePicker.ITimePickerListener
import com.tristaam.todo.utils.timePicker.TimePickerDialog
import java.text.ParseException

class CreateTaskFragment : Fragment() {
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateTaskViewModel by viewModels {
        CreateTaskViewModel.CreateTaskViewModelFactory(requireContext())
    }
    private val subtaskAdapter: SubtaskAdapter by lazy {
        SubtaskAdapter(object : ISubtaskListener {
            override fun onTick(position: Int) {
                if (position == -1) {
                    return
                }
                viewModel.updateSubtaskStatus(position)
                subtaskAdapter.setData(viewModel.subtasks)
                subtaskAdapter.notifyItemChanged(position)
            }

            override fun onDelete(position: Int) {
                if (position == -1) {
                    return
                }
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
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.GONE
        binding.apply {
            initPriorityViewGroup()
            toolbar.setNavigationOnClickListener { onBackPressed() }
            btnCreateTask.setOnClickListener { onBtnCreateTaskClick() }
            chipDate.setOnClickListener { onChipDateClick() }
            chipTime.setOnClickListener { onChipTimeClick() }
            clPriority.setOnClickListener { onClPriorityClick() }
            clProject.setOnClickListener { onClProjectClick() }
            clAddSubtask.setOnClickListener { onClAddSubtaskClick() }
            rvSubtasks.adapter = subtaskAdapter
            rvSubtasks.layoutManager = LinearLayoutManager(context)
        }
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
                    taskId = 0,
                    status = false
                )
            )
            subtaskAdapter.setData(viewModel.subtasks)
            subtaskAdapter.notifyItemInserted(viewModel.subtasks.size - 1)
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
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
        TimePickerDialog.show(childFragmentManager, "Time picker", object : ITimePickerListener {
            override fun onTimeSelected(hour: Int, minute: Int) {
                binding.chipTime.text = getString(R.string.time_format, hour, minute)
            }
        })
    }

    private fun onBtnCreateTaskClick() {
        try {
            binding.apply {
                if (etTaskTitle.text.toString().isEmpty() ||
                    etDescription.text.toString().isEmpty()
                ) {
                    throw Exception()
                } else {
                    viewModel.insertTask(
                        Task(
                            title = etTaskTitle.text.toString(),
                            description = etDescription.text.toString(),
                            projectId = viewModel.projectId,
                            dueDate = DateTimeUtils.dateTimeFormat.parse("${chipDate.text} ${chipTime.text}")!!,
                            status = false,
                            priority = viewModel.priority.value!!
                        )
                    )
                }
            }
            findNavController().popBackStack()
        } catch (e: Exception) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }
}