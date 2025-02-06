package org.example.project.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.project.domain.ApiResult
import org.example.project.domain.Task
import org.example.project.model.CreateTaskRequest
import org.example.project.model.LoginRequest
import org.example.project.model.LoginResponse
import org.example.project.model.UpdateTaskRequest
import org.example.project.model.UpdateTaskStatusRequest
import org.example.project.storage.TokenStorage
import org.example.project.utils.baseUrl


class KtorClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                encodeDefaults = true  // Importante para valores por defecto
                coerceInputValues = true
            })
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        install(Logging) {  // Añadir esto para ver los logs
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
    private var userEmail: String? = null
    private val apiUrl = baseUrl
    companion object {
        private var token: String? = null
        private var userEmail: String? = null
    }
    private val tokenStorage: TokenStorage = TokenStorage()
    init {
        // Inicializar el token desde el storage al crear el cliente
        tokenStorage.getToken()?.let {
            saveToken(it)
        }
        tokenStorage.getEmail()?.let {
            saveUserEmail(it)
        }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: ResponseException) {
            when (e) {
                is ClientRequestException -> {
                    when (e.response.status.value) {
                        401 -> Result.failure(Exception("Unauthorized: Invalid credentials"))
                        else -> Result.failure(Exception("Error: ${e.response.status.description}"))
                    }
                }
                else -> Result.failure(Exception("Network error: ${e.message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unknown error: ${e.message}"))
        }
    }

    fun saveToken(newToken: String) {
        token = newToken
        tokenStorage.saveToken(newToken)
    }
    private fun getCurrentToken(): String? {
        if (token == null) {
            token = tokenStorage.getToken()
        }
        return token
    }


    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        return try {
            val response = client.post("$apiUrl/auth/login") {
                setBody(LoginRequest(email, password))
            }.body<LoginResponse>()
            saveUserEmail(response.email)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

// Actualiza los demás métodos de manera similar

    suspend fun getTasks(): ApiResult<List<Task>> {
        return try {
            val currentToken = getCurrentToken()
            val response = client.get("$apiUrl/tasks") {
                header(HttpHeaders.Authorization, "Bearer $currentToken")
            }.body<List<Task>>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun createTask(name: String, description: String, imageBytes: ByteArray? = null): ApiResult<Task> {
        return try {
            val currentToken = getCurrentToken()

            val multipartBody = MultiPartFormDataContent(
                formData {
                    append("name", name)
                    append("description", description)
                    append("completed", "false")

                    if (imageBytes != null) {
                        append(
                            "image",
                            imageBytes,
                            Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=image.jpg")
                            }
                        )
                    }
                }
            )

            val response = client.post("$apiUrl/tasks") {
                header(HttpHeaders.Authorization, "Bearer $currentToken")
                setBody(multipartBody)
            }

            val task = response.body<Task>()
            ApiResult.Success(task)
        } catch (e: Exception) {
            println("Error al crear tarea: ${e.message}")
            e.printStackTrace()
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateTask(id: Int, name: String, description: String): ApiResult<Task> {
        return try {
            val currenToken = getCurrentToken()
            val response = client.patch("$apiUrl/tasks/$id") {
                header(HttpHeaders.Authorization, "Bearer $currenToken")
                setBody(UpdateTaskRequest(name, description))
            }.body<Task>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateTaskStatus(id: Int, completed: Boolean): ApiResult<Task> {
        return try {
            val currentToken = getCurrentToken()
            val response = client.patch("$apiUrl/tasks/$id") {
                header(HttpHeaders.Authorization, "Bearer $currentToken")
                setBody(UpdateTaskStatusRequest(completed))
            }.body<Task>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun deleteTask(id: Int): ApiResult<Task> {
        return try {
            val currentToken = getCurrentToken()
            val response = client.delete("$apiUrl/tasks/$id") {
                header(HttpHeaders.Authorization, "Bearer $currentToken")
            }.body<Task>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    fun saveUserEmail(email: String) {
        userEmail = email
        tokenStorage.saveEmail(email)
    }

    fun getUserEmail(): String? {
        if (userEmail == null) {
            userEmail = tokenStorage.getEmail()
        }
        return userEmail
    }
}

@Serializable
data class ErrorResponse(
    val message: String,
    val error: String,
    val statusCode: Int
)

