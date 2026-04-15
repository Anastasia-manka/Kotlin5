package com.example.kt5_2.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt5_2.domain.AddPhotoUseCase
import com.example.kt5_2.domain.DeletePhotoUseCase
import com.example.kt5_2.domain.GetAllPhotosUseCase
import com.example.kt5_2.domain.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class PhotoViewModel(
    private val getAllPhotosUseCase: GetAllPhotosUseCase,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _isLoading.value = true
            _photos.value = getAllPhotosUseCase()
            _isLoading.value = false
        }
    }

    fun addPhoto(file: File) {
        viewModelScope.launch {
            val newPhoto = addPhotoUseCase(file)
            _photos.value = listOf(newPhoto) + _photos.value
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            deletePhotoUseCase(photo)
            _photos.value = _photos.value.filter { it.id != photo.id }
        }
    }
}