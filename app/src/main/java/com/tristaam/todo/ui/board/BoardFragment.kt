package com.tristaam.todo.ui.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tristaam.todo.R
import com.tristaam.todo.adapter.project.ProjectAdapter
import com.tristaam.todo.adapter.task.ITaskListener
import com.tristaam.todo.adapter.task.TaskAdapter
import com.tristaam.todo.databinding.FragmentBoardBinding
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.createTask.ICreateTaskListener
import com.tristaam.todo.ui.dialog.CreateProjectDialogFragment
import com.tristaam.todo.ui.dialog.ICreateProjectListener
import com.tristaam.todo.viewmodel.ProjectViewModel
import com.tristaam.todo.viewmodel.TaskViewModel

class BoardFragment : Fragment() {
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    private val rotateClockwise: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_clockwise
        )
    }
    private val rotateCounterClockwise: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_counter_clockwise
        )
    }
    private val fadeIn: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_in
        )
    }
    private val fadeOut: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_out
        )
    }
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModel.TaskViewModelFactory(requireContext())
    }
    private val projectViewModel: ProjectViewModel by viewModels {
        ProjectViewModel.ProjectViewModelFactory(requireContext())
    }
    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskAdapter = TaskAdapter(
            object : ITaskListener {
                override fun onTaskClicked(task: Task) {
                    val bundle = Bundle()
                    bundle.putParcelable(getString(R.string.task_detail), task)
                    findNavController().navigate(R.id.taskDetailFragment, bundle)
                }
            },
            requireContext()
        )
        val projectAdapter = ProjectAdapter(requireContext())
        binding.apply {
            rvTasks.adapter = taskAdapter
            rvTasks.layoutManager = LinearLayoutManager(context)
            taskViewModel.getAllTasks().observe(viewLifecycleOwner) {
                taskAdapter.setData(it)
            }
            rvProjects.adapter = projectAdapter
            rvProjects.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            projectViewModel.getAllProjects().observe(viewLifecycleOwner) {
                projectAdapter.setData(it)
            }
            btnAdd.setOnClickListener { onBtnAddClick() }
            btnAddTask.setOnClickListener { onBtnAddTaskClick() }
            btnAddProject.setOnClickListener { onBtnAddProjectClick() }
        }
    }

    private fun onBtnAddClick() {
        binding.apply {
            clicked = if (!clicked) {
                btnAdd.startAnimation(rotateClockwise)
                btnAddTask.visibility = View.VISIBLE
                btnAddProject.visibility = View.VISIBLE
                btnAddTask.startAnimation(fadeIn)
                btnAddProject.startAnimation(fadeIn)
                true
            } else {
                btnAdd.startAnimation(rotateCounterClockwise)
                btnAddTask.visibility = View.GONE
                btnAddProject.visibility = View.GONE
                btnAddTask.startAnimation(fadeOut)
                btnAddProject.startAnimation(fadeOut)
                false
            }
        }
    }

    private fun onBtnAddTaskClick() {
        val bundle = Bundle()
        bundle.putSerializable(getString(R.string.add_task), object : ICreateTaskListener {
            override fun onCreateTask(task: Task) {
                taskViewModel.insertTask(task)
            }
        })
        findNavController().navigate(R.id.createTaskFragment, bundle)
    }

    private fun onBtnAddProjectClick() {
        CreateProjectDialogFragment(object : ICreateProjectListener {
            override fun onCreateProject(project: Project) {
                projectViewModel.insertProject(project)
            }

        }).show(childFragmentManager, "CreateProjectDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}