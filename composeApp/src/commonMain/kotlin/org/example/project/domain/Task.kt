package org.example.project.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// domain/model/Task.kt
// domain/Task.kt
@Serializable
data class Task(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val completed: Boolean = false,
    @SerialName("userId")
    val userId: Int = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val deletedAt: String? = null
)