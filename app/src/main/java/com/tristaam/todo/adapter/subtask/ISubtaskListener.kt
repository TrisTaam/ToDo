package com.tristaam.todo.adapter.subtask


interface ISubtaskListener {
    fun onTick(position: Int)
    fun onDelete(position: Int)
    fun onSave(position: Int, newName: String)
}