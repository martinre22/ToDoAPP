package com.example.todoapp.addtasks.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun TasksScreen(viewModel: TasksViewModel) {

    val showDialog: Boolean by viewModel.showDialog.observeAsState(initial = false)

    Box(modifier = Modifier.fillMaxSize()) {
        AddTaskDialog(
            show = showDialog,
            onDismiss = {
                viewModel.onDialogClose()
            },
            onTaskAdded = { viewModel.onTaskCreated(it) }
        )
        FabDialog(Modifier.align(Alignment.BottomEnd), viewModel)
        TasksList(viewModel)
    }
}

@Composable
private fun TasksList(viewModel: TasksViewModel) {
    val myTasks: List<TaskModelUI> = viewModel.listTasks
    LazyColumn {
        items(myTasks, key = { it.idTask }) { task ->
            ItemTask(taskModelUI = task, viewModel = viewModel)
        }
    }
}

@Composable
private fun ItemTask(taskModelUI: TaskModelUI, viewModel: TasksViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        elevation = 8.dp
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
