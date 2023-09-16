package com.tristaam.todo.adapter.task

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tristaam.todo.R
import com.tristaam.todo.databinding.HolderTaskBinding
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Task
import com.tristaam.todo.utils.DateTimeUtils

class TaskAdapter(private val listener: ITaskListener, private val context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private val tasks = mutableListOf<Task>()

    inner class TaskViewHolder(private val binding: HolderTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title
                tvDescription.text = task.description
                tvDueDate.text = DateTimeUtils.dateFormat.format(task.dueDate)
                when (task.priority) {
                    Priority.URGENT -> ivFlag.setImageDrawable(context.resources.getDrawable(R.drawable.ic_flag_red))
                    Priority.HIGH -> ivFlag.setImageDrawable(context.resources.getDrawable(R.drawable.ic_flag_orange))
                    Priority.MEDIUM -> ivFlag.setImageDrawable(context.resources.getDrawable(R.drawable.ic_flag_yellow))
                    Priority.LOW -> ivFlag.setImageDrawable(context.resources.getDrawable(R.drawable.ic_flag_green))
                }
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