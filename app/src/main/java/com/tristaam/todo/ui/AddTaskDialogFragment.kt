package com.tristaam.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tristaam.todo.databinding.FragmentAddTaskDialogBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Task

class AddTaskDialogFragment(private val listener: IAddTaskListener) : BottomSheetDialogFragment() {
    private var _binding: FragmentAddTaskDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnMedium.isChecked = true
            btnDueDate.setOnClickListener { }
            btnCreateTask.setOnClickListener { onCreateTaskClick() }
        }
    }

    private fun onDueDateClick() {

    }

    private fun onCreateTaskClick() {
        binding.apply {
            val priority = when {
                btnHigh.isChecked -> Priority.HIGH
                btnMedium.isChecked -> Priority.MEDIUM
                else -> Priority.LOW
            }
            listener.addTask(
                Task(
                    etTaskName.text.toString(),
                    etTaskDescription.text.toString(),
                    priority
                )
            )
        }
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}