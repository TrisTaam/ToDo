package com.tristaam.todo.ui.dialog.projectDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tristaam.todo.R
import com.tristaam.todo.databinding.FragmentProjectDetailDialogBinding
import com.tristaam.todo.model.Project

class ProjectDetailDialogFragment(private val projectId: Int) : BottomSheetDialogFragment() {
    private var _binding: FragmentProjectDetailDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProjectDetailViewModel by lazy {
        ProjectDetailViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.getProject(projectId).observe(viewLifecycleOwner) { project ->
                etProjectName.setText(project.name)
                btnProjectSave.setOnClickListener { onProjectSaved(project) }
                clDeleteProject.setOnClickListener { onDeleteProjectClick(project) }
            }
        }
    }

    private fun onDeleteProjectClick(project: Project) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete))
            setMessage(getString(R.string.delete_project_alert))
            setPositiveButton("Yes") { _, _ ->
                viewModel.deleteProject(project)
                dismiss()
            }
            setNegativeButton("No") { _, _ ->
                dismiss()
            }
            show()
        }
    }

    private fun onProjectSaved(project: Project) {
        project.name = binding.etProjectName.text.toString()
        viewModel.updateProject(project)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}