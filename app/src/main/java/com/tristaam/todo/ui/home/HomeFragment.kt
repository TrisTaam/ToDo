package com.tristaam.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tristaam.todo.R
import com.tristaam.todo.adapter.TaskAdapter
import com.tristaam.todo.databinding.FragmentHomeBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.AddTaskDialogFragment
import com.tristaam.todo.ui.IAddTaskListener
import com.tristaam.todo.ui.ITaskListener
import com.tristaam.todo.viewmodel.TaskViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.TaskViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskAdapter = TaskAdapter(object : ITaskListener {
            override fun detailTask(task: Task) {
                val bundle = Bundle()
                bundle.putParcelable("task", task)
                findNavController().navigate(R.id.taskDetailFragment, bundle)
            }
        })
        binding.apply {
            rvAllTasks.layoutManager = LinearLayoutManager(context)
            rvAllTasks.adapter = taskAdapter
            viewModel.getAllTasks().observe(viewLifecycleOwner) {
                taskAdapter.setData(it)
            }
            btnAddTask.setOnClickListener { onAddTaskClick() }
        }
    }

    private fun onAddTaskClick() {
        AddTaskDialogFragment(object : IAddTaskListener {
            override fun addTask(task: Task) {
                viewModel.insertTask(task)
            }
        }).show(childFragmentManager, "AddTaskDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}