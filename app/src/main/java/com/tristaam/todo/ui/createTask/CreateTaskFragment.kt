package com.tristaam.todo.ui.createTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
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

class CreateTaskFragment : Fragment() {
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private var _priorityText: String? = null
    private val priorityText get() = _priorityText!!
    private val viewModel: CreateTaskViewModel by viewModels {
        CreateTaskViewModel.CreateTaskViewModelFactory(requireContext())
    }
    private val subtaskAdapter: SubtaskAdapter by lazy {
        SubtaskAdapter(object : ISubtaskListener {
            override fun onTick(subtask: Subtask, isChecked: Boolean) {
                subtask.status = isChecked
                viewModel.updateSubtask(subtask)
            }

            override fun onDelete(subtask: Subtask) {
                viewModel.deleteSubtask(subtask)
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

    private fun onClAddSubtaskClick() {
        binding.apply {
            viewModel.insertSubtask(
                Subtask(
                    "",
                    0,
                    false
                )
            )
            viewModel.getAllSubtasks().observe(viewLifecycleOwner) {
                subtaskAdapter.setData(it)
            }
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
                binding.apply {
                    _priorityText = priority.priorityName
                    tvPriority.text = getString(R.string.priority_selected, priorityText)
                }
            }
        }).show(childFragmentManager, "Select priority")
    }

    private fun onChipDateClick() {
        // Fixing later
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .build()
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.chipDate.text =
                DateTimeUtils.inputDateFormat.parse(materialDatePicker.headerText)
                    ?.let { it1 -> DateTimeUtils.dateFormat.format(it1) }
        }
        materialDatePicker.show(childFragmentManager, "Date picker")
    }

    private fun onChipTimeClick() {
        val materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        materialTimePicker.addOnPositiveButtonClickListener {
            binding.chipTime.text =
                getString(R.string.time_format, materialTimePicker.hour, materialTimePicker.minute)
        }
        materialTimePicker.show(childFragmentManager, "Time picker")
    }

    private fun onBtnCreateTaskClick() {
        try {
            binding.apply {
                viewModel.insertTask(
                    Task(
                        etTaskTitle.text.toString(),
                        etDescription.text.toString(),
                        viewModel.projectId,
                        DateTimeUtils.inputFormat.parse("${chipDate.text} ${chipTime.text}")!!,
                        false,
                        Priority.valueOf(priorityText.uppercase())
                    )
                )
            }
            findNavController().popBackStack()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        _binding = null
        _priorityText = null
    }
}