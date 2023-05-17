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

    suspend fun add(taskModelUI: TaskModelUI) {
        taskDao.addTask(TaskEntity(taskModelUI.idTask, taskModelUI.task, taskModelUI.selected))
    }

    suspend fun update(taskModel: TaskModelUI) {
        taskDao.updateTask(TaskEntity(taskModel.idTask, taskModel.task, taskModel.selected))
    }
}
