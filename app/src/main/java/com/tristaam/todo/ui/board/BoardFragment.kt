package com.tristaam.todo.ui.board

import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.database.adapter.task.ITaskListener
import com.tristaam.todo.database.adapter.task.TaskAdapter
import com.tristaam.todo.databinding.FragmentBoardBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.createTask.IAddTaskListener
import com.tristaam.todo.viewmodel.TaskViewModel

class BoardFragment : Fragment() {
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.TaskViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskAdapter = TaskAdapter(object : ITaskListener {
            override fun onTaskClicked(task: Task) {
                val bundle = Bundle()
                bundle.putParcelable(getString(R.string.task_detail), task)
                findNavController().navigate(R.id.taskDetailFragment, bundle)
            }
        })
        binding.apply {
            rvTasks.adapter = taskAdapter
            rvTasks.layoutManager = LinearLayoutManager(context)
            viewModel.getAllTasks().observe(viewLifecycleOwner) {
                taskAdapter.setData(it)
            }
            btnAdd.setOnClickListener { onBtnAddClick() }
        }
    }

    private fun onBtnAddClick() {
        val bundle = Bundle()
        bundle.putSerializable(getString(R.string.add_task), object : IAddTaskListener {
            override fun onAddTask(task: Task) {
                viewModel.insertTask(task)
            }
        })
        findNavController().navigate(R.id.createTaskFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}