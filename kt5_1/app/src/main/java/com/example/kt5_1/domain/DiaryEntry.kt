package com.example.kt5_1.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Модель данных
data class DiaryEntry(
    val fileName: String,
    val title: String,
    val content: String,
    val timestamp: Long
) {
    val preview: String
        get() = if (content.length > 40) content.take(40) + "..." else content
}

// UseCase: получение всех записей
class GetAllEntriesUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(): List<DiaryEntry> = repository.getAllEntries()
}

// UseCase: добавление записи
class AddEntryUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(title: String, content: String): DiaryEntry =
        repository.saveEntry(title, content)
}

// UseCase: удаление записи
class DeleteEntryUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(fileName: String) = repository.deleteEntry(fileName)
}

// Интерфейс репозитория (для связи с data слоем)
interface DiaryRepository {
    suspend fun getAllEntries(): List<DiaryEntry>
    suspend fun saveEntry(title: String, content: String): DiaryEntry
    suspend fun deleteEntry(fileName: String)
}