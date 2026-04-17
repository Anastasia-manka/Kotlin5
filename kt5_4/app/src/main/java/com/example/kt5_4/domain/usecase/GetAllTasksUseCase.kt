package com.example.kt5_4.domain.usecase

import com.example.kt5_4.domain.model.Task
import com.example.kt5_4.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getAllTasks()
    }
}