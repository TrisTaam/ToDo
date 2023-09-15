package com.tristaam.todo.ui.taskdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.databinding.FragmentTaskDetailBinding
import com.tristaam.todo.model.Task
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
            btnSaveTask.setOnClickListener { task?.let { it1 -> onSaveClick(it1) } }
            btnDeleteTask.setOnClickListener { task?.let { it1 -> onDeleteClick(it1) } }
        }
    }

    private fun onSaveClick(task: Task) {
        binding.apply {
            task.title = etTaskTitle.text.toString()
            task.description = etDescription.text.toString()
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