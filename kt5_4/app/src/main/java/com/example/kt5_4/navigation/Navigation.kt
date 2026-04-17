package com.example.kt5_4.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kt5_4.presentation.ui.screen.AddEditTaskScreen
import com.example.kt5_4.presentation.ui.screen.TaskListScreen
import com.example.kt5_4.presentation.viewmodel.TaskViewModel

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object AddEditTask : Screen("add_edit_task/{taskId}") {
        fun passId(id: Int = -1): String {
            return "add_edit_task/$id"
        }
    }
}

@Composable
fun TaskNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    val tasks by viewModel.tasks.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.AddEditTask.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            val task = if (taskId != -1) {
                tasks.find { it.id == taskId }
            } else null
            AddEditTaskScreen(
                navController = navController,
                viewModel = viewModel,
                task = task
            )
        }
    }
}