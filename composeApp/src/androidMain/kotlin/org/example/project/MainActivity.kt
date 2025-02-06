package org.example.project

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.example.project.ui.PlatformCameraLauncher



class MainActivity : ComponentActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 100
        private const val CAMERA_PERMISSION = "android.permission.CAMERA"
    }

    private var pendingCameraLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlatformCameraLauncher.setActivity(this)
        setContent {
            App()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PlatformCameraLauncher.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        PlatformCameraLauncher.setActivity(null)
    }

    fun checkAndRequestCameraPermission(onPermissionGranted: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                CAMERA_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted
                onPermissionGranted()
            }
            else -> {
                // Request the permission
                pendingCameraLaunch = true
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(CAMERA_PERMISSION),
                    CAMERA_PERMISSION_REQUEST
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingCameraLaunch) {
                    pendingCameraLaunch = false
                    PlatformCameraLauncher.launchCamera()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AppAndroidPreview() {
    App()
}
