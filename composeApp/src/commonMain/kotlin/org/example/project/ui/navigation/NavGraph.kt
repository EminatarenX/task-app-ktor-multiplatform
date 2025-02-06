package org.example.project.ui.navigation

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.example.project.presentation.login.LoginScreen
import org.example.project.presentation.tasks.TasksScreen

@Composable
fun NavGraph() {
    val navigator = rememberNavigator()

    NavHost(
        navigator = navigator,
        initialRoute = "/login"
    ) {
        scene("/login") {
            LoginScreen(
                onNavigateToTasks = { navigator.navigate("/tasks") }
            )
        }

        scene("/tasks") {
            TasksScreen()
        }
    }
}