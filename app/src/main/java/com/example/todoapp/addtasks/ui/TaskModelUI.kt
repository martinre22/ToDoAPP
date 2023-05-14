package com.example.todoapp.addtasks.ui

data class TaskModelUI(
    val idTask: Long = System.currentTimeMillis(),
    val task: String,
    val selected: Boolean = false
)
