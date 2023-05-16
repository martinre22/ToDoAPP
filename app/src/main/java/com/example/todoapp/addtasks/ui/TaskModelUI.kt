package com.example.todoapp.addtasks.ui

data class TaskModelUI(
    val idTask: Int = System.currentTimeMillis().hashCode(),
    val task: String,
    val selected: Boolean = false
)
