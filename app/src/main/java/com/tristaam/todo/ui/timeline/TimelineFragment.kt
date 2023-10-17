package com.tristaam.todo.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tristaam.todo.R
import com.tristaam.todo.adapter.task.ITaskListener
import com.tristaam.todo.adapter.task.TaskAdapter
import com.tristaam.todo.databinding.FragmentTimelineBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.taskdetail.TaskDetailFragment
import com.tristaam.todo.utils.DateTimeUtils
import java.text.ParseException
import java.util.Calendar

class TimelineFragment : Fragment() {
    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TimelineViewModel by viewModels {
        TimelineViewModel.TimelineViewModelFactory(requireContext())
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            changeDate(day, month, year)
            rvTasks.adapter = taskAdapter
            rvTasks.layoutManager = LinearLayoutManager(context)
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                changeDate(dayOfMonth, month + 1, year)
            }
            viewModel.tasks.observe(viewLifecycleOwner) {
                taskAdapter.setData(it)
            }
            calendarView.date = System.currentTimeMillis()
        }
    }

    private fun changeDate(day: Int, month: Int, year: Int) {
        try {
            val startDate = DateTimeUtils.dateTimeFormat.parse(
                String.format(
                    "%02d/%02d/%d 00:00",
                    day,
                    month,
                    year
                )
            )
            val endDate = DateTimeUtils.dateTimeFormat.parse(
                String.format(
                    "%02d/%02d/%d 23:59",
                    day,
                    month,
                    year
                )
            )
            viewModel.setTasksByDate(startDate!!, endDate!!, viewLifecycleOwner)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}