package com.tristaam.todo.ui.dialog.selectProject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tristaam.todo.adapter.project.IProjectListener
import com.tristaam.todo.adapter.project.ProjectAdapter
import com.tristaam.todo.databinding.FragmentSelectProjectDialogBinding
import com.tristaam.todo.model.Project
import com.tristaam.todo.ui.dialog.createProject.CreateProjectDialogFragment

class SelectProjectDialogFragment(private val listener: ISelectProjectListener) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentSelectProjectDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SelectProjectViewModel by viewModels {
        SelectProjectViewModel.SelectProjectViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectProjectDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val projectAdapter = ProjectAdapter(requireContext(), object : IProjectListener {
            override fun onProjectClick(project: Project) {
                listener.onSelectProject(project.id)
                dismiss()
            }
        })
        binding.apply {
            rvProjects.adapter = projectAdapter
            rvProjects.layoutManager = LinearLayoutManager(requireContext())
            viewModel.getAllProjects().observe(viewLifecycleOwner) {
                projectAdapter.setData(it)
            }
            clCreateProject.setOnClickListener { onCreateProjectClick() }
        }
    }

    private fun onCreateProjectClick() {
        CreateProjectDialogFragment().show(childFragmentManager, "Create project")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}