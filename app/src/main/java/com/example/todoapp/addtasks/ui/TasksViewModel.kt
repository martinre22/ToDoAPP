package com.example.todoapp.addtasks.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.addtasks.domain.AddTaskUseCase
import com.example.todoapp.addtasks.domain.GetTasksUseCase
import com.example.todoapp.addtasks.ui.TaskUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private var _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private var _listTasks = mutableStateListOf<TaskModelUI>()
    val listTasks: List<TaskModelUI> = _listTasks
    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated(task: String) {
        _showDialog.value = false
        _listTasks.add(TaskModelUI(task = task))
        viewModelScope.launch {
            addTaskUseCase(TaskModelUI(task = task))
        }
    }

    fun onShowDialogCLick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModelUI: TaskModelUI) {
        val index = _listTasks.indexOf(taskModelUI)
        _listTasks[index] = _listTasks[index].let {
            it.copy(selected = !it.selected)
        }
    }

    fun onItemRemove(taskModelUI: TaskModelUI) {
        val task = _listTasks.find { it.idTask == taskModelUI.idTask }
        _listTasks.remove(task)
    }
}
