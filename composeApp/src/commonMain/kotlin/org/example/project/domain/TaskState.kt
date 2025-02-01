package org.example.project.domain

sealed class TaskState {
    object Loading : TaskState()
    data class Success(val tasks: List<Task>) : TaskState()
    data class Error(val message: String) : TaskState()
}