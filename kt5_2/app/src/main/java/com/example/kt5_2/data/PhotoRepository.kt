package com.example.kt5_2.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.kt5_2.domain.Photo
import com.example.kt5_2.domain.PhotoRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.os.Environment

class PhotoRepositoryImpl(private val context: Context) : PhotoRepository {

    private val photosDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: context.filesDir

    override suspend fun getAllPhotos(): List<Photo> {
        return photosDir.listFiles()
            ?.filter { it.isFile && it.extension.lowercase() in listOf("jpg", "jpeg", "png") }
            ?.map { file ->
                Photo(
                    id = file.name,
                    fileName = file.name,
                    filePath = file.absolutePath,
                    uri = Uri.fromFile(file),
                    timestamp = file.lastModified()
                )
            }
            ?.sortedByDescending { it.timestamp }
            ?: emptyList()
    }

    override suspend fun savePhoto(file: File): Photo {

        val destFile = File(photosDir, file.name)
        if (!destFile.exists()) {
            file.copyTo(destFile, overwrite = true)
        }

        return Photo(
            id = destFile.name,
            fileName = destFile.name,
            filePath = destFile.absolutePath,
            uri = Uri.fromFile(destFile),
            timestamp = destFile.lastModified()
        )
    }

    override suspend fun deletePhoto(photo: Photo) {
        File(photo.filePath).delete()
    }

    override suspend fun exportToGallery(photo: Photo): Boolean {
        return try {
            val sourceFile = File(photo.filePath)
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, sourceFile.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/MyAppExported")
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { output ->
                    sourceFile.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        fun generateFileName(): String {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            return "IMG_$timestamp.jpg"
        }
    }
}
