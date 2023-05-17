package com.example.todoapp.addtasks.data

import com.example.todoapp.addtasks.ui.TaskModelUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val tasks: Flow<List<TaskModelUI>> = taskDao.getTasks().map { items ->
        items.map { TaskModelUI(it.idTask, it.task, it.selected) }
    }

    suspend fun add(taskModel: TaskModelUI) {
        taskDao.addTask(taskModel.toData())
    }

    suspend fun update(taskModel: TaskModelUI) {
        taskDao.updateTask(taskModel.toData())
    }

    suspend fun delete(taskModel: TaskModelUI) {
        taskDao.deleteTask(taskModel.toData())
    }
}

fun TaskModelUI.toData(): TaskEntity = TaskEntity(this.idTask, this.task, this.selected)
