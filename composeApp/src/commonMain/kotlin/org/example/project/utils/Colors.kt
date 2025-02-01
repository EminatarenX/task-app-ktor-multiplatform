package org.example.project.utils

import androidx.compose.ui.graphics.Color

// utils/Colors.kt
object AppColors {
    val Primary = Color(0xFF3B82F6) // Azul principal
    val Background = Color(0xFF1E1B24) // Fondo oscuro
    val CardBackground = Color.hsl(234f, 0.17f, 0.12f)
    val Error = Color(0xFFDC2626)
    val Success = Color(0xFF22C55E)
    val TextPrimary = Color.White
    val TextSecondary = Color.White.copy(alpha = 0.7f)
    val IconTint = Color.White
    val Border = Color.White.copy(alpha = 0.1f)
}