package com.tristaam.todo.adapter.project

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tristaam.todo.databinding.HolderProjectBinding
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Task

class ProjectAdapter(private val context: Context) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    private val projects = mutableListOf<Project>()

    inner class ProjectViewHolder(private val binding: HolderProjectBinding) :
        ViewHolder(binding.root) {
        fun bind(project: Project) {
            binding.apply {
                tvProjectName.text = project.name
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectAdapter.ProjectViewHolder {
        return ProjectViewHolder(
            HolderProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ProjectViewHolder, position: Int) {
        holder.bind(projects[position])
    }

    override fun getItemCount(): Int = projects.size

    fun setData(projects: List<Project>) {
        val diffResult = DiffUtil.calculateDiff(ProjectDiffUtilCallback(this.projects, projects))
        diffResult.dispatchUpdatesTo(this)
        this.projects.clear()
        this.projects.addAll(projects)
    }

    class ProjectDiffUtilCallback(
        private val oldList: List<Project>,
        private val newList: List<Project>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}