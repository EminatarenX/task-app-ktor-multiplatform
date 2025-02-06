package org.example.project.ui

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import org.example.project.MainActivity
import java.io.ByteArrayOutputStream

actual object PlatformCameraLauncher {
    private var currentCallback: ((ByteArray) -> Unit)? = null
    private var currentActivity: Activity? = null
    private const val CAMERA_REQUEST_CODE = 1001

    actual fun takePicture(onImageCaptured: (ByteArray) -> Unit) {
        currentCallback = onImageCaptured
        currentActivity?.let { activity ->
            (activity as MainActivity).checkAndRequestCameraPermission {
                launchCamera()
            }
        }
    }

    fun launchCamera() {
        currentActivity?.let { activity ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? android.graphics.Bitmap
            imageBitmap?.let { bitmap ->
                val stream = ByteArrayOutputStream()
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, stream)
                currentCallback?.invoke(stream.toByteArray())
                currentCallback = null
            }
        }
    }

    fun setActivity(activity: Activity?) {
        currentActivity = activity
    }
}