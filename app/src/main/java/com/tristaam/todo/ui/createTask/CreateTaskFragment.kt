package com.tristaam.todo.ui.createTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.databinding.FragmentCreateTaskBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Task
import com.tristaam.todo.utils.DateTimeUtils

class CreateTaskFragment : Fragment() {
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: ICreateTaskListener

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
        listener = arguments?.getSerializable(getString(R.string.add_task)) as ICreateTaskListener
        binding.apply {
            btnCreateTask.setOnClickListener { onBtnCreateTaskClick() }
            chipDate.setOnClickListener { onChipDateClick() }
            chipTime.setOnClickListener { onChipTimeClick() }
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

    private fun onBtnCreateTaskClick() {
        try {
            binding.apply {
                listener.onCreateTask(
                    Task(
                        etTaskTitle.text.toString(),
                        etDescription.text.toString(),
                        DateTimeUtils.inputFormat.parse("${chipDate.text} ${chipTime.text}")!!,
                        Priority.MEDIUM
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
    }
}