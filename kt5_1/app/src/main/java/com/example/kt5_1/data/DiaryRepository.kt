package com.example.kt5_1.data

import android.content.Context
import com.example.kt5_1.domain.DiaryEntry
import com.example.kt5_1.domain.DiaryRepository
import java.io.File

// Интерфейс репозитория
interface DiaryRepository {
    suspend fun getAllEntries(): List<DiaryEntry>
    suspend fun saveEntry(title: String, content: String): DiaryEntry
    suspend fun deleteEntry(fileName: String)
}

// Реализация репозитория
class DiaryRepositoryImpl(private val context: Context) : DiaryRepository {

    private val diaryDir: File = context.filesDir

    override suspend fun getAllEntries(): List<DiaryEntry> {
        return diaryDir.listFiles()
            ?.filter { it.isFile && it.extension == "txt" }
            ?.map { file ->
                val content = file.readText()
                val title = content.substringBefore("\n").takeIf { it.isNotEmpty() } ?: "Без названия"
                val body = content.substringAfter("\n", "")
                DiaryEntry(
                    fileName = file.name,
                    title = title,
                    content = body,
                    timestamp = file.lastModified()
                )
            }
            ?.sortedByDescending { it.timestamp }
            ?: emptyList()
    }

    override suspend fun saveEntry(title: String, content: String): DiaryEntry {
        val timestamp = System.currentTimeMillis()
        val fileName = "${timestamp}_${title.take(20).replace(" ", "_")}.txt"
        val file = File(diaryDir, fileName)
        file.writeText("$title\n$content")
        return DiaryEntry(
            fileName = fileName,
            title = title,
            content = content,
            timestamp = timestamp
        )
    }

    override suspend fun deleteEntry(fileName: String) {
        File(diaryDir, fileName).delete()
    }
}