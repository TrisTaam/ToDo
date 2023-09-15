package com.tristaam.todo.database.adapter.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tristaam.todo.databinding.HolderTaskBinding
import com.tristaam.todo.model.Task

class TaskAdapter(private val listener: ITaskListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private val tasks = mutableListOf<Task>()

    inner class TaskViewHolder(private val binding: HolderTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title
                tvDescription.text = task.description
                clTask.setOnClickListener { listener.onTaskClicked(task) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            HolderTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        return holder.bind(tasks[position])
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