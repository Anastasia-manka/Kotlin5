package com.example.kt5_4.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt5_4.data.preferences.PreferencesDataStore
import com.example.kt5_4.domain.model.Task
import com.example.kt5_4.domain.usecase.AddTaskUseCase
import com.example.kt5_4.domain.usecase.DeleteTaskUseCase
import com.example.kt5_4.domain.usecase.GetAllTasksUseCase
import com.example.kt5_4.domain.usecase.ImportTasksUseCase
import com.example.kt5_4.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isDarkForCompleted = MutableStateFlow(false)
    val isDarkForCompleted: StateFlow<Boolean> = _isDarkForCompleted.asStateFlow()

    init {
        loadTasks()
        observeColorSetting()
        checkAndImportTasks()
    }

    private fun loadTasks() {
        getAllTasksUseCase()
            .onEach { taskList ->
                _tasks.value = taskList
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    private fun observeColorSetting() {
        preferencesDataStore.isDarkForCompletedTasks
            .onEach { isDark ->
                _isDarkForCompleted.value = isDark
            }
            .launchIn(viewModelScope)
    }

    private fun checkAndImportTasks() {
        viewModelScope.launch {
            val currentTasks = _tasks.value
            if (currentTasks.isEmpty()) {
                _isLoading.value = true
                importTasksUseCase()
                loadTasks()
            }
        }
    }

    fun addTask(title: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            addTaskUseCase(title, description)
            _isLoading.value = false
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        updateTask(updatedTask)
    }

    fun setCompletedTaskColor(isDark: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.setCompletedTaskColor(isDark)
        }
    }
}