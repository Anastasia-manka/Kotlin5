package com.example.kt5_4.domain.usecase

import com.example.kt5_4.domain.model.Task
import com.example.kt5_4.domain.repository.TaskRepository

class AddTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(title: String, description: String? = null): Task {
        val task = Task(
            title = title,
            description = description,
            isCompleted = false
        )
        repository.addTask(task)
        return task
    }
}