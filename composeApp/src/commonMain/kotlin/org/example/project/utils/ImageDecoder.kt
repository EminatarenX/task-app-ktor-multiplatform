package org.example.project.utils
import androidx.compose.ui.graphics.ImageBitmap

expect object ImageDecoder {
    fun decodeBase64Image(base64String: String): ImageBitmap?
}