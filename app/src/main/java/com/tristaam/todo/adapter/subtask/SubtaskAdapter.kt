package com.tristaam.todo.adapter.subtask

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
                checkBoxSubtask.isChecked = subTask.status
                etSubtaskName.setText(subTask.name)
                checkBoxSubtask.setOnClickListener {
                    etSubtaskName.clearFocus()
                    listener.onTick(adapterPosition)
                }
                btnDeleteSubtask.setOnClickListener {
                    listener.onDelete(adapterPosition)
                }
                etSubtaskName.setOnFocusChangeListener { _, isFocused ->
                    if (!isFocused) {
                        listener.onSave(adapterPosition, etSubtaskName.text.toString())
                    }
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
        this.subtasks.clear()
        this.subtasks.addAll(subtasks)
    }
}