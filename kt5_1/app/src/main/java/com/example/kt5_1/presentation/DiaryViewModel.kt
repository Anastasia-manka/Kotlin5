package com.example.kt5_1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt5_1.domain.AddEntryUseCase
import com.example.kt5_1.domain.DeleteEntryUseCase
import com.example.kt5_1.domain.DiaryEntry
import com.example.kt5_1.domain.DiaryRepository
import com.example.kt5_1.domain.GetAllEntriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiaryViewModel(
    private val getAllEntriesUseCase: GetAllEntriesUseCase,
    private val addEntryUseCase: AddEntryUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase
) : ViewModel() {

    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            _isLoading.value = true
            _entries.value = getAllEntriesUseCase()
            _isLoading.value = false
        }
    }

    fun addEntry(title: String, content: String) {
        viewModelScope.launch {
            if (title.isNotBlank() || content.isNotBlank()) {
                val newEntry = addEntryUseCase(
                    title = title.ifBlank { "Без названия" },
                    content = content
                )
                _entries.value = listOf(newEntry) + _entries.value
            }
        }
    }

    fun deleteEntry(fileName: String) {
        viewModelScope.launch {
            deleteEntryUseCase(fileName)
            _entries.value = _entries.value.filter { it.fileName != fileName }
        }
    }
}