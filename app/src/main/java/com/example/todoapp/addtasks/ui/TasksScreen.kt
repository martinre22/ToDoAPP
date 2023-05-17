package com.example.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

@Composable
fun TasksScreen(viewModel: TasksViewModel) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val showDialog: Boolean by viewModel.showDialog.observeAsState(initial = false)

    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                delay(1500)
                value = it
            }
        }
    }

    when (uiState) {
        is TaskUiState.Error -> {}
        is TaskUiState.Loading -> {
            CircularProgress()
        }
        is TaskUiState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                AddTaskDialog(
                    show = showDialog,
                    onDismiss = {
                        viewModel.onDialogClose()
                    },
                    onTaskAdded = { viewModel.onTaskCreated(it) }
                )
                FabDialog(Modifier.align(Alignment.BottomEnd), viewModel)
                TasksList((uiState as TaskUiState.Success).tasks, viewModel)
            }
        }
    }
}

@Composable
private fun CircularProgress() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.height(80.dp).width(80.dp),
            color = Color.DarkGray
        )
    }
}

@Composable
private fun TasksList(tasks: List<TaskModelUI>, viewModel: TasksViewModel) {

    LazyColumn {
        items(tasks, key = { it.idTask }) { task ->
            ItemTask(taskModelUI = task, viewModel = viewModel)
        }
    }
}

@Composable
private fun ItemTask(taskModelUI: TaskModelUI, viewModel: TasksViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    viewModel.onItemRemove(taskModelUI)
                })
            },
        elevation = 4.dp
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
            Text(
                text = taskModelUI.task,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
            Checkbox(
                checked = taskModelUI.selected,
                onCheckedChange = { viewModel.onCheckBoxSelected(taskModelUI) }
            )
        }
    }
}

@Composable
private fun FabDialog(pModifier: Modifier, viewModel: TasksViewModel) {
    FloatingActionButton(
        onClick = { viewModel.onShowDialogCLick() },
        modifier = pModifier.padding(16.dp)
    ) {
        Icon(Icons.Filled.Add, "")
    }
}

@Composable
private fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {

    var myTask by remember { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    "Añade tu tarea",
                    fontSize = 16.sp,
                    modifier = Modifier.align(CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = myTask,
                    onValueChange = { myTask = it },
                    maxLines = 1,
                    singleLine = true
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        onTaskAdded(myTask)
                        myTask = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}
