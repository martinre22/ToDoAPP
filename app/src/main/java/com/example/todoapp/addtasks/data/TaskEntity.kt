package com.example.todoapp.addtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey
    val idTask: Int,
    val task: String,
    val selected: Boolean = false
)
