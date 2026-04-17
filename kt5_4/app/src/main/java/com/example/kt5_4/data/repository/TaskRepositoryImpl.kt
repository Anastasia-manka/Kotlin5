package com.example.kt5_4.data.repository

import android.content.Context
import com.example.kt5_4.R
import com.example.kt5_4.data.local.TaskDao
import com.example.kt5_4.data.local.TaskDatabase
import com.example.kt5_4.data.local.toDomain
import com.example.kt5_4.data.local.toEntity
import com.example.kt5_4.data.preferences.PreferencesDataStore
import com.example.kt5_4.domain.model.Task
import com.example.kt5_4.domain.repository.TaskRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val context: Context,
    private val preferencesDataStore: PreferencesDataStore
) : TaskRepository {

    private val taskDao: TaskDao = TaskDatabase.getDatabase(context).taskDao()

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun importTasksFromJson(): Boolean {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.tasks)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val type = object : TypeToken<List<TaskImportDto>>() {}.type
            val tasks: List<TaskImportDto> = Gson().fromJson(jsonString, type)

            tasks.forEach { taskDto ->
                val task = Task(
                    title = taskDto.title,
                    description = taskDto.description,
                    isCompleted = taskDto.isCompleted
                )
                taskDao.insertTask(task.toEntity())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun isFirstLaunch(): Boolean {
        val count = taskDao.getTaskCount()
        return count == 0
    }

    private data class TaskImportDto(
        val title: String,
        val description: String?,
        val isCompleted: Boolean
    )
}