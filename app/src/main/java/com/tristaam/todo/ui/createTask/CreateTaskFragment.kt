package com.tristaam.todo.ui.createTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tristaam.todo.MainActivity
import com.tristaam.todo.R
import com.tristaam.todo.databinding.FragmentCreateTaskBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Task

class CreateTaskFragment : Fragment() {
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: IAddTaskListener

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
        listener = arguments?.getSerializable(getString(R.string.add_task)) as IAddTaskListener
        binding.apply {
            btnCreateTask.setOnClickListener { onBtnCreateTaskClick() }
        }
    }

    private fun onBtnCreateTaskClick() {
        binding.apply {
            listener.onAddTask(
                Task(
                    etTaskTitle.text.toString(),
                    etDescription.text.toString(),
                    Priority.MEDIUM
                )
            )
        }
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }
}