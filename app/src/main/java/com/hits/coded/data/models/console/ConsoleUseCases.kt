package com.hits.coded.data.models.console

import com.hits.coded.domain.useCases.console.*

data class ConsoleUseCases(
    val checkIsInputAvailableUseCase: CheckIsInputAvailableUseCase,
    val clearConsoleUseCase: ClearConsoleUseCase,
    val getBufferUseCase: GetBufferUseCase,
    val readFromConsoleUseCase: ReadFromConsoleUseCase,
    val writeToConsoleUseCase: WriteToConsoleUseCase
)