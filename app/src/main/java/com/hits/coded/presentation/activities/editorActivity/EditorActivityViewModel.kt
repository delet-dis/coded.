package com.hits.coded.presentation.activities.editorActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.console.ConsoleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorActivityViewModel @Inject constructor(private val consoleUseCases: ConsoleUseCases) :
    ViewModel() {
    private var counter = 0

    init{
        viewModelScope.launch {
            consoleUseCases.getBufferUseCase.getBuffer().collect {
                Log.d("CONSOLE_DEBUG", "current buffer:\n" + it.toString())
            }
        }

        viewModelScope.launch {
            consoleUseCases.checkIsInputAvailableUseCase.checkIsInputAvailable().collect {
                Log.d("CONSOLE_DEBUG", "input available: " + it.toString())
                if(it) {
                    delay(1000)
                    consoleUseCases.writeToConsoleUseCase.writeToConsole("I_WRITE_IT" + counter.toString())
                }
            }
        }
    }

    fun writeSmthToConsole() {
        counter++
        consoleUseCases.writeToConsoleUseCase.writeToConsole("Smth" + counter.toString())
    }

    fun clearConsole() {
        consoleUseCases.clearConsoleUseCase.clearConsole()
    }

    fun readFromConsole() {
        viewModelScope.launch {
            val input = consoleUseCases.readFromConsoleUseCase.readFromConsole()
            Log.d("CONSOLE_DEBUG", "I get: " + input)
        }

    }

}