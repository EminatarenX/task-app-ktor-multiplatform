package org.example.project.ui

actual object PlatformCameraLauncher {
    actual fun takePicture(onImageCaptured: (ByteArray) -> Unit) {
        // iOS implementation would go here
        // For now, just return an empty byte array
        onImageCaptured(ByteArray(0))
    }
}