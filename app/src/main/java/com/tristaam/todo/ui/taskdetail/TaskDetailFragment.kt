package com.tristaam.todo.ui.taskdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.databinding.FragmentTaskDetailBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.utils.DateTimeUtils
import com.tristaam.todo.viewmodel.TaskViewModel

class TaskDetailFragment : Fragment() {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.TaskViewModelFactory(requireContext())
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
        val task = arguments?.getParcelable<Task>(getString(R.string.task_detail))
        Log.d("TaskDetailFragment", "onViewCreated: $task")
        binding.apply {
            etTaskTitle.setText(task?.title)
            etDescription.setText(task?.description)
            chipDate.text = task?.dueDate?.let { DateTimeUtils.dateFormat.format(it) }
            chipTime.text = task?.dueDate?.let { DateTimeUtils.timeFormat.format(it) }
            chipDate.setOnClickListener { onChipDateClick() }
            chipTime.setOnClickListener { onChipTimeClick() }
            btnSaveTask.setOnClickListener { task?.let { it1 -> onSaveClick(it1) } }
            btnDeleteTask.setOnClickListener { task?.let { it1 -> onDeleteClick(it1) } }
        }
    }

    private fun onChipDateClick() {
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .build()
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.chipDate.text = materialDatePicker.headerText
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

    private fun onSaveClick(task: Task) {
        binding.apply {
            task.title = etTaskTitle.text.toString()
            task.description = etDescription.text.toString()
            task.dueDate = DateTimeUtils.inputFormat.parse(
                "${chipDate.text} ${chipTime.text}"
            )!!
        }
        viewModel.updateTask(task)
        findNavController().popBackStack()
    }

    private fun onDeleteClick(task: Task) {
        viewModel.deleteTask(task)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }
}