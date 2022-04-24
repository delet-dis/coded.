package com.hits.coded.presentation.activities.editorActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.console.ConsoleUseCases
import com.hits.coded.data.repositoriesImplementations.ConsoleRepositoryImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorActivityViewModel : ViewModel() {
    val c = ConsoleRepositoryImplementation()

    init {
        viewModelScope.launch {
            c.buffer.collect {
                Log.d("CONSOLE_DEBUG", it.size.toString())
            }
        }
    }

    fun writeSmthToConsole() {
        viewModelScope.launch {
            c.writeToConsole("Smth")
        }
    }
}