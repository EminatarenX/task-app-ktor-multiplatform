package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.example.project.presentation.login.LoginScreen
import org.example.project.presentation.tasks.TasksScreen
import org.example.project.utils.AppColors

@Composable
@Preview
fun App() {
    PreComposeApp {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppColors.Background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing) //owInsetsPadding o usando una configuración diferente
                //.windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                val colors = getColorTheme()
                AppTheme {
                    val navigator = rememberNavigator()
                    NavHost(
                        navigator = navigator,
                        initialRoute = "/login"
                    ) {
                        scene("/login") {
                            LoginScreen(
                                onNavigateToTasks = {
                                    navigator.navigate("/tasks")
                                }
                            )
                        }

                        scene("/tasks") {
                            TasksScreen()
                        }
                    }
                }
            }
        }
    }
}