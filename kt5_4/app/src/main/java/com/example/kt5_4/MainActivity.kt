package com.example.kt5_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kt5_4.ui.theme.Kt5_4Theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kt5_4.data.preferences.PreferencesDataStore
import com.example.kt5_4.data.repository.TaskRepositoryImpl
import com.example.kt5_4.domain.usecase.AddTaskUseCase
import com.example.kt5_4.domain.usecase.DeleteTaskUseCase
import com.example.kt5_4.domain.usecase.GetAllTasksUseCase
import com.example.kt5_4.domain.usecase.ImportTasksUseCase
import com.example.kt5_4.domain.usecase.UpdateTaskUseCase
import com.example.kt5_4.navigation.TaskNavigation
import com.example.kt5_4.presentation.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskApp()
                }
            }
        }
    }
}

@Composable
fun TaskApp() {
    val context = LocalContext.current
    val preferencesDataStore = PreferencesDataStore(context)
    val repository = TaskRepositoryImpl(context, preferencesDataStore)

    val getAllTasksUseCase = GetAllTasksUseCase(repository)
    val addTaskUseCase = AddTaskUseCase(repository)
    val updateTaskUseCase = UpdateTaskUseCase(repository)
    val deleteTaskUseCase = DeleteTaskUseCase(repository)
    val importTasksUseCase = ImportTasksUseCase(repository)

    val viewModel: TaskViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(
                    getAllTasksUseCase,
                    addTaskUseCase,
                    updateTaskUseCase,
                    deleteTaskUseCase,
                    importTasksUseCase,
                    preferencesDataStore
                ) as T
            }
        }
    )

    TaskNavigation(viewModel = viewModel)
}