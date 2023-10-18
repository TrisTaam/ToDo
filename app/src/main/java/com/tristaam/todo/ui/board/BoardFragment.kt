package com.tristaam.todo.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.tristaam.todo.R
import com.tristaam.todo.adapter.task.ITaskListener
import com.tristaam.todo.adapter.task.TaskAdapter
import com.tristaam.todo.databinding.FragmentBoardBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.completedTask.CompletedTaskFragment
import com.tristaam.todo.ui.dialog.createProject.CreateProjectDialogFragment
import com.tristaam.todo.ui.dialog.projectDetail.ProjectDetailDialogFragment
import com.tristaam.todo.ui.dialog.sortTask.SortTaskDialogFragment
import com.tristaam.todo.ui.taskdetail.TaskDetailFragment
import com.tristaam.todo.utils.observeOnce

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
    private val fadeIn100: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_in_100
        )
    }
    private val fadeOut100: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_out_100
        )
    }
    private val fadeIn200: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_in_200
        )
    }
    private val fadeOut200: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fade_out_200
        )
    }
    private val viewModel: BoardViewModel by viewModels {
        BoardViewModel.BoardViewModelFactory(requireContext())
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
                    if (task.status) {
                        viewModel.incCompletedTasksCount()
                        val projectId = binding.cgProjects.checkedChipId
                        if (projectId > 0) {
                            viewModel.getUncompletedTasksByProjectId(projectId)
                                .observeOnce(viewLifecycleOwner) {
                                    viewModel.setTasks(it, projectId, viewLifecycleOwner)
                                }
                        }
                    } else {
                        viewModel.decCompletedTasksCount()
                    }
                }
            },
            requireContext()
        )
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
        binding.apply {
            rvTasks.adapter = taskAdapter
            rvTasks.layoutManager = LinearLayoutManager(context)
            initChipGroup()
            initToolBarActionButtons()
            viewModel.completedTasksCount.observe(viewLifecycleOwner) { completedTasksCount ->
                tvProgress.text =
                    getString(R.string.progress, completedTasksCount, viewModel.tasksCount)
                val percent = if (viewModel.tasksCount == 0) {
                    0
                } else {
                    (completedTasksCount * 1f / viewModel.tasksCount * 100).toInt()
                }
                tvProgressPercent.text = getString(R.string.progress_percent, percent)
                pbProgress.progress = percent
            }
            viewModel.tasks.observe(viewLifecycleOwner) {
                taskAdapter.setData(it)
            }
            btnAdd.setOnClickListener { onBtnAddClick() }
            btnAddTask.setOnClickListener { onBtnAddTaskClick() }
            btnAddProject.setOnClickListener { onBtnAddProjectClick() }
            clProgress.setOnClickListener { onProgressClick() }
        }
    }

    private fun initToolBarActionButtons() {
        binding.apply {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_sort -> {
                        SortTaskDialogFragment().show(
                            childFragmentManager,
                            "SortTaskDialogFragment"
                        )
                        true
                    }

                    R.id.action_search -> {
//                        SearchTaskDialogFragment().show(
//                            childFragmentManager,
//                            "SearchTaskDialogFragment"
//                        )
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun onProgressClick() {
        val bundle = Bundle()
        bundle.putInt(CompletedTaskFragment.PROJECT_ID, binding.cgProjects.checkedChipId)
        findNavController().navigate(R.id.completedTaskFragment, bundle)
    }

    private fun initChipGroup() {
        binding.apply {
            viewModel.getAllProjects().observe(viewLifecycleOwner) {
                cgProjects.removeAllViews()
                val chipAll = Chip(context).apply {
                    id = 0
                    text = getString(R.string.all)
                    isCheckable = true
                    isChecked = true
                    chipAllBehavior(this)
                }
                chipAll.setOnClickListener { view ->
                    val chipView = (view as Chip)
                    if (chipView.isChecked) {
                        chipAllBehavior(chipView)
                    } else {
                        chipView.isChecked = true
                    }
                }
                cgProjects.addView(chipAll)
                it.forEach { project ->
                    val chip = Chip(context).apply {
                        id = project.id
                        text = project.name
                        isCheckable = true
                    }
                    chip.setOnClickListener { view -> onChipClick(view as Chip) }
                    cgProjects.addView(chip)
                }
            }
        }
    }

    private fun chipAllBehavior(chip: Chip) {
        binding.apply {
            binding.toolbar.title = chip.text
            viewModel.getAllTasks().observeOnce(viewLifecycleOwner) {
                viewModel.setTasks(it, 0, viewLifecycleOwner)
            }
        }
    }

    private fun chipBehavior(chip: Chip) {
        binding.apply {
            binding.toolbar.title = chip.text
            viewModel.getUncompletedTasksByProjectId(chip.id).observeOnce(viewLifecycleOwner) {
                viewModel.setTasks(it, chip.id, viewLifecycleOwner)
            }
        }
    }

    private fun onChipClick(chip: Chip) {
        if (chip.isChecked) {
            chipBehavior(chip)
            return
        }
        ProjectDetailDialogFragment(chip.id).show(
            childFragmentManager,
            "ProjectDetailDialogFragment"
        )
        chip.isChecked = true
    }

    private fun onBtnAddClick() {
        binding.apply {
            clicked = if (!clicked) {
                btnAdd.startAnimation(rotateClockwise)
                btnAddTask.visibility = View.VISIBLE
                tvAddTask.visibility = View.VISIBLE
                btnAddProject.visibility = View.VISIBLE
                tvAddProject.visibility = View.VISIBLE
                btnAddTask.startAnimation(fadeIn100)
                tvAddTask.startAnimation(fadeIn100)
                btnAddProject.startAnimation(fadeIn200)
                tvAddProject.startAnimation(fadeIn200)
                true
            } else {
                btnAdd.startAnimation(rotateCounterClockwise)
                btnAddTask.visibility = View.GONE
                tvAddTask.visibility = View.GONE
                btnAddProject.visibility = View.GONE
                tvAddProject.visibility = View.GONE
                btnAddTask.startAnimation(fadeOut100)
                tvAddTask.startAnimation(fadeOut100)
                btnAddProject.startAnimation(fadeOut200)
                tvAddProject.startAnimation(fadeOut200)
                false
            }
        }
    }

    private fun onBtnAddTaskClick() {
        findNavController().navigate(R.id.createTaskFragment)
    }

    private fun onBtnAddProjectClick() {
        CreateProjectDialogFragment().show(childFragmentManager, "CreateProjectDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clicked = false
        _binding = null
    }
}