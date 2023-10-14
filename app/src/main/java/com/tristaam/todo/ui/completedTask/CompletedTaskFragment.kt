package com.tristaam.todo.ui.completedTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tristaam.todo.R
import com.tristaam.todo.adapter.task.ITaskListener
import com.tristaam.todo.adapter.task.TaskAdapter
import com.tristaam.todo.databinding.FragmentCompletedTaskBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.taskdetail.TaskDetailFragment
import com.tristaam.todo.utils.observeOnce

class CompletedTaskFragment : Fragment() {
    private var _binding: FragmentCompletedTaskBinding? = null
    val binding get() = _binding!!
    private val viewModel: CompletedTaskViewModel by viewModels {
        CompletedTaskViewModel.CompletedTaskViewModelFactory(requireContext())
    }
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(
            object : ITaskListener {
                override fun onTaskClicked(task: Task) {
                    val bundle = Bundle()
                    bundle.putInt(TaskDetailFragment.TASK_ID, task.id)
                    findNavController().navigate(R.id.taskDetailFragment, bundle)
                }

                override fun onTaskTick(task: Task) {
                    task.status = !task.status
                    viewModel.updateTask(task)
                }
            },
            requireContext()
        )
    }

    companion object {
        const val PROJECT_ID = "projectId"
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
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setNavigationOnClickListener { onBackPressed() }
            viewModel.projectId = arguments?.getInt(PROJECT_ID) ?: 0
            if (viewModel.projectId > 0) {
                viewModel.getProject(viewModel.projectId).observeOnce(viewLifecycleOwner) {
                    toolbar.title = getString(R.string.completed_tasks, it.name)
                }
            } else {
                toolbar.title = getString(R.string.completed_tasks, getString(R.string.all))
            }
            rvCompletedTask.adapter = taskAdapter
            rvCompletedTask.layoutManager = LinearLayoutManager(context)
            viewModel.getTasks().observe(viewLifecycleOwner) {
                taskAdapter.setData(it)
            }
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}