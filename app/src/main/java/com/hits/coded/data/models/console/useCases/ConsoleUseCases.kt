package com.hits.coded.data.models.console.useCases

import com.hits.coded.domain.useCases.console.CheckIsInputAvailableUseCase
import com.hits.coded.domain.useCases.console.ClearConsoleUseCase
import com.hits.coded.domain.useCases.console.GetBufferUseCase
import com.hits.coded.domain.useCases.console.ReadFromConsoleUseCase
import com.hits.coded.domain.useCases.console.WriteToConsoleUseCase

data class ConsoleUseCases(
    val checkIsInputAvailableUseCase: CheckIsInputAvailableUseCase,
    val clearConsoleUseCase: ClearConsoleUseCase,
    val getBufferUseCase: GetBufferUseCase,
    val readFromConsoleUseCase: ReadFromConsoleUseCase,
    val writeToConsoleUseCase: WriteToConsoleUseCase
)