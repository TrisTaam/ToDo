package com.tristaam.todo.adapter.subtask

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tristaam.todo.databinding.HolderSubtaskBinding
import com.tristaam.todo.model.Subtask

class SubtaskAdapter(private val listener: ISubtaskListener) :
    RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder>() {
    private val subtasks = mutableListOf<Subtask>()

    inner class SubtaskViewHolder(private val binding: HolderSubtaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subTask: Subtask) {
            binding.apply {
                checkBoxSubtask.addOnCheckedStateChangedListener { _, isChecked ->
                    listener.onTick(subTask, isChecked == 1)
                }
                btnDeleteSubtask.setOnClickListener {
                    listener.onDelete(subTask)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        return SubtaskViewHolder(
            HolderSubtaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = subtasks.size

    override fun onBindViewHolder(holder: SubtaskViewHolder, position: Int) {
        holder.bind(subtasks[position])
    }

    fun setData(subtasks: List<Subtask>) {
        val diffResult = DiffUtil.calculateDiff(SubtaskDiffUtilCallback(this.subtasks, subtasks))
        diffResult.dispatchUpdatesTo(this)
        this.subtasks.clear()
        this.subtasks.addAll(subtasks)
    }

    class SubtaskDiffUtilCallback(
        private val oldList: List<Subtask>,
        private val newList: List<Subtask>
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