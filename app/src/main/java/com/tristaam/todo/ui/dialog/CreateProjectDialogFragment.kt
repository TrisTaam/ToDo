package com.tristaam.todo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tristaam.todo.databinding.FragmentCreateProjectDialogBinding
import com.tristaam.todo.model.Project

class CreateProjectDialogFragment(private val listener: ICreateProjectListener) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentCreateProjectDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProjectDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnCreateProject.setOnClickListener { onBtnCreateProjectClick() }
        }
    }

    private fun onBtnCreateProjectClick() {
        binding.apply {
            listener.onCreateProject(Project(etProjectName.text.toString()))
        }
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}