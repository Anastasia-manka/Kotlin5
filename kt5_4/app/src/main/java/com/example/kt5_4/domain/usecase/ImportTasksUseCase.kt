package com.example.kt5_4.domain.usecase

import com.example.kt5_4.domain.repository.TaskRepository

class ImportTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.importTasksFromJson()
    }
}