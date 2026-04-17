package com.example.kt5_4.domain.usecase

import com.example.kt5_4.domain.model.Task
import com.example.kt5_4.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}