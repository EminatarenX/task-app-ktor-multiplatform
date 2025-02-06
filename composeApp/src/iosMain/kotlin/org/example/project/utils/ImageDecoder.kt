package org.example.project.utils

import androidx.compose.ui.graphics.ImageBitmap

actual object ImageDecoder {
    actual fun decodeBase64Image(base64String: String): ImageBitmap? {
        return null // Implementar la lógica específica de iOS si es necesario
    }
}