package org.example.project.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageData(
    val url: String,
    val image: String  // Now it's base64 string instead of ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ImageData
        return url == other.url
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}
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
    val deletedAt: String? = null,
    val image: ImageData? = null
)
