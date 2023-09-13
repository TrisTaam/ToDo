package com.tristaam.todo.ui.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tristaam.todo.databinding.FragmentTaskDetailBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Task
import com.tristaam.todo.viewmodel.TaskViewModel

class TaskDetailFragment : Fragment() {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
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
        val task = arguments?.getParcelable<Task>("task")
        binding.apply {
            toolbar.setNavigationOnClickListener { onBack() }
            etTaskDetailName.setText(task?.name)
            etTaskDetailDescription.setText(task?.description)
            when (task?.priority) {
                Priority.HIGH -> btnDetailHigh.isChecked = true
                Priority.MEDIUM -> btnDetailMedium.isChecked = true
                else -> btnDetailLow.isChecked = true
            }
            btnDetailSave.setOnClickListener { task?.let { it1 -> onSaveClick(it1) } }
            btnDetailDelete.setOnClickListener { task?.let { it1 -> onDeleteClick(it1) } }
        }
    }

    private fun onBack() {
        findNavController().popBackStack()
    }

    private fun onSaveClick(task: Task) {
        binding.apply {
            task.name = etTaskDetailName.text.toString()
            task.description = etTaskDetailDescription.text.toString()
            task.priority = when {
                btnDetailHigh.isChecked -> Priority.HIGH
                btnDetailMedium.isChecked -> Priority.MEDIUM
                else -> Priority.LOW
            }
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
        _binding = null
    }
}
