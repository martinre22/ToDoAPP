package com.example.todoapp.addtasks.ui

sealed interface TaskUiState {
    object Loading : TaskUiState
    data class Error(val throwable: Throwable) : TaskUiState
    data class Success(val tasks: List<TaskModelUI>) : TaskUiState
}
