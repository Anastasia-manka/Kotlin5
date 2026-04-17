package com.example.kt5_4.domain.repository

import com.example.kt5_4.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    // Получить все задачи (Flow для автоматического обновления)
    fun getAllTasks(): Flow<List<Task>>

    // Получить задачу по ID
    suspend fun getTaskById(id: Int): Task?

    // Добавить задачу
    suspend fun addTask(task: Task)

    // Обновить задачу
    suspend fun updateTask(task: Task)

    // Удалить задачу
    suspend fun deleteTask(task: Task)

    // Импортировать задачи из JSON (разовый импорт)
    suspend fun importTasksFromJson(): Boolean

    // Проверить, нужно ли импортировать задачи при первом запуске
    suspend fun isFirstLaunch(): Boolean
}