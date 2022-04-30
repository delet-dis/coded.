package com.hits.coded.presentation.views.console.viewModel

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
    consoleUseCases: ConsoleUseCases
) {
    private val _consoleBuffer = consoleUseCases.getBufferUseCase.getBuffer().asLiveData()
    val consoleBuffer: LiveData<ArrayDeque<String>>
        get() = _consoleBuffer
}