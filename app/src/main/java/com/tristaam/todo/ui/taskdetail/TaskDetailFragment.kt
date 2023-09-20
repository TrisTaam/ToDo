package com.tristaam.todo.ui.taskdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tristaam.todo.MainActivity
import com.tristaam.todo.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment() {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }
}