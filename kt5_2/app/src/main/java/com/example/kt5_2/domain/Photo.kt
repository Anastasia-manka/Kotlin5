package com.example.kt5_2.domain

import android.net.Uri
import java.io.File

// Модель данных
data class Photo(
    val id: String,
    val fileName: String,
    val filePath: String,
    val uri: Uri,
    val timestamp: Long
) {
    val preview: String
        get() = fileName
}

// UseCase: получение всех фото
class GetAllPhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(): List<Photo> = repository.getAllPhotos()
}

// UseCase: добавление фото
class AddPhotoUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(file: File): Photo = repository.savePhoto(file)
}

// UseCase: удаление фото
class DeletePhotoUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(photo: Photo) = repository.deletePhoto(photo)
}

class ExportPhotoUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(photo: Photo): Boolean = repository.exportToGallery(photo)
}

// Интерфейс репозитория
interface PhotoRepository {
    suspend fun getAllPhotos(): List<Photo>
    suspend fun savePhoto(file: File): Photo
    suspend fun deletePhoto(photo: Photo)
    suspend fun exportToGallery(photo: Photo): Boolean
}