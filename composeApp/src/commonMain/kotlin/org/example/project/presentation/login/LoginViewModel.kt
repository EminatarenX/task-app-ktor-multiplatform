package org.example.project.presentation.login

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.data.KtorClient
import org.example.project.domain.ApiResult
import org.example.project.domain.LoginState
import org.example.project.storage.TokenStorage

// presentation/login/LoginViewModel.kt
class LoginViewModel(
    private val api: KtorClient = KtorClient(),
    private val tokenStorage: TokenStorage = TokenStorage()
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                when (val response = api.login(email, password)) {
                    is ApiResult.Success -> {
                        api.saveToken(response.data.accessToken)
                        api.saveUserEmail(response.data.email)
                        tokenStorage.saveToken(response.data.accessToken)
                        tokenStorage.saveEmail(response.data.email)
                        _loginState.value = LoginState.Success
                    }
                    is ApiResult.Error -> {
                        _loginState.value = LoginState.Error(response.message)
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }
}