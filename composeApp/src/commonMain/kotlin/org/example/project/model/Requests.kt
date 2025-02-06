package org.example.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("access_token")
    val accessToken: String,
    val email: String
)


@Serializable
data class CreateTaskRequest(
    val name: String,
    val description: String,
    val completed: Boolean = false
)

@Serializable
data class UpdateTaskRequest(
    val name: String,
    val description: String
)

@Serializable
data class UpdateTaskStatusRequest(
    val completed: Boolean
)