package org.example.project.presentation.tasks

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.domain.Task
import org.example.project.ui.DeleteConfirmationDialog
import org.example.project.ui.TaskCard
import org.example.project.ui.TaskDialog
import org.example.project.utils.AppColors
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import moe.tlaster.precompose.viewmodel.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
import org.example.project.ui.TaskDetailDialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TasksScreen(
    viewModel: TaskViewModel = viewModel(modelClass = TaskViewModel::class) { TaskViewModel() }
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hello, ${userEmail.ifEmpty { "User" }}") },
                backgroundColor = AppColors.Background,
                contentColor = AppColors.TextPrimary,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                backgroundColor = AppColors.Primary
            ) {
                Icon(Icons.Default.Add, "Add Task")
            }
        },
        backgroundColor = AppColors.Background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading && tasks.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppColors.Primary
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        tasks,
                        key = { it.id }
                    ) { task ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                when (dismissValue) {
                                    DismissValue.DismissedToEnd -> {
                                        selectedTask = task
                                        showDeleteDialog = true
                                        false
                                    }
                                    DismissValue.DismissedToStart -> {
                                        viewModel.updateTaskStatus(task.id, !task.completed)
                                        false
                                    }
                                    DismissValue.Default -> false
                                }
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = { DismissBackground(dismissState) },
                            dismissContent = {
                                TaskCard(
                                    task = task,
                                    onTaskClick = {
                                        selectedTask = task
                                        showDetailDialog = true
                                    }
                                )
                            }
                        )
                    }
                }
            }

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = AppColors.Error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }

    // Dialog para crear nueva nota
    if (showCreateDialog) {
        TaskDialog(
            isOpen = true,
            onClose = { showCreateDialog = false },
            onSave = { name, description, imageBase64 ->
                viewModel.createTask(name, description, imageBase64)
                showCreateDialog = false
            }
        )
    }

    // Dialog para ver detalle de nota
    if (showDetailDialog && selectedTask != null) {
        TaskDetailDialog(
            task = selectedTask!!,
            isOpen = true,
            onClose = {
                showDetailDialog = false
                selectedTask = null
            }
        )
    }

    // Dialog de confirmación para eliminar
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            isOpen = true,
            onConfirm = {
                selectedTask?.let { viewModel.deleteTask(it.id) }
                showDeleteDialog = false
                selectedTask = null
            },
            onDismiss = {
                showDeleteDialog = false
                selectedTask = null
            }
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DismissBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.dismissDirection) {
            DismissDirection.StartToEnd -> AppColors.Error
            DismissDirection.EndToStart -> AppColors.Success
            null -> Color.Transparent
        }
    )

    val alignment = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
        null -> Alignment.Center
    }

    val icon = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Icons.Default.Delete
        DismissDirection.EndToStart -> Icons.Default.Check
        null -> Icons.Default.Clear
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}