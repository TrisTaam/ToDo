package com.tristaam.todo.model

enum class Priority(val priorityName: String, val weight: Int) {
    URGENT("Urgent", 3),
    HIGH("High", 2),
    MEDIUM("Medium", 1),
    LOW("Low", 0)
}