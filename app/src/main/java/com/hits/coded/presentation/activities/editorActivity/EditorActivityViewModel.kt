package com.hits.coded.presentation.activities.editorActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.console.ConsoleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorActivityViewModel @Inject constructor(private val consoleUseCases: ConsoleUseCases) :
    ViewModel() {

    init{
        viewModelScope.launch {
            consoleUseCases.getBufferUseCase.getBuffer().collect {
                Log.d("CONSOLE_DEBUG", it.size.toString())
            }
        }
    }

    fun writeSmthToConsole() {
        viewModelScope.launch {
            consoleUseCases.writeToConsoleUseCase.writeToConsole("Smth")
        }
    }
}