package org.example.project.presentation.tasks

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.data.KtorClient
import org.example.project.domain.ApiResult
import org.example.project.domain.Task
import org.example.project.domain.TaskState
import org.example.project.storage.TokenStorage


// presentation/tasks/TaskViewModel.kt
class TaskViewModel(
    private val api: KtorClient = KtorClient(),
    private val tokenStorage: TokenStorage = TokenStorage()
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _userEmail = MutableStateFlow<String>("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        tokenStorage.getToken()?.let { token ->
            api.saveToken(token)
        }
        tokenStorage.getEmail()?.let { email ->
            api.saveUserEmail(email)
            _userEmail.value = email
        }
        loadTasks()
        _userEmail.value = api.getUserEmail() ?: ""
        tokenStorage.getToken()?.let { token ->
            api.saveToken(token)
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = api.getTasks()) {
                    is ApiResult.Success -> {
                        _tasks.value = result.data
                        _error.value = null
                    }
                    is ApiResult.Error -> {
                        _error.value = result.message
                        _tasks.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _tasks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTask(name: String, description: String, imageBase64: ByteArray? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = api.createTask(name, description, imageBase64)) {
                    is ApiResult.Success -> {
                        loadTasks()
                        _error.value = null
                    }
                    is ApiResult.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateTask(id: Int, name: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = api.updateTask(id, name, description)) {
                    is ApiResult.Success -> {
                        loadTasks()
                        _error.value = null
                    }
                    is ApiResult.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun updateTaskStatus(id: Int, completed: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = api.updateTaskStatus(id, completed)) {
                    is ApiResult.Success -> {
                        loadTasks()
                        _error.value = null
                    }
                    is ApiResult.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = api.deleteTask(id)) {
                    is ApiResult.Success -> {
                        loadTasks()
                        _error.value = null
                    }
                    is ApiResult.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}