package com.hits.coded.presentation.views.console.viewModel

import android.text.SpannableStringBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class ConsoleViewModel @Inject constructor(
    private val consoleUseCases: ConsoleUseCases
) {
    private val _consoleBuffer = consoleUseCases.getBufferUseCase.getBuffer().asLiveData()
    val consoleBuffer: LiveData<SpannableStringBuilder>
        get() = _consoleBuffer

    private val _isAvailableToInput =
        consoleUseCases.checkIsInputAvailableUseCase.checkIsInputAvailable().asLiveData()
    val isAvailableToInput: LiveData<Boolean>
        get() = _isAvailableToInput

    fun submitStringToConsole(input: String) =
        consoleUseCases.writeToConsoleUseCase.writeInputToConsole(input)
}