package com.tristaam.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tristaam.todo.databinding.HolderAllTasksBinding
import com.tristaam.todo.model.Task
import com.tristaam.todo.ui.ITaskListener

class TaskAdapter(private val listener: ITaskListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var tasks: MutableList<Task> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            HolderAllTasksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(private val binding: HolderAllTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                tvTaskName.text = task.name
                tvTaskPriority.text = task.priority.priorityName
                cardTask.setOnClickListener { listener.detailTask(task) }
            }
        }
    }

    fun setData(tasks: List<Task>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffUtilCallback(this.tasks, tasks))
        diffResult.dispatchUpdatesTo(this)
        this.tasks.clear()
        this.tasks.addAll(tasks)
    }

    class TaskDiffUtilCallback(private val oldList: List<Task>, private val newList: List<Task>) :
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