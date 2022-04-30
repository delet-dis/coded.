package com.hits.coded.domain.useCases.console

import android.graphics.Color
import com.hits.coded.domain.repositories.ConsoleRepository

class WriteToConsoleUseCase(private val consoleRepository: ConsoleRepository) {
    fun writeToConsole(input: String, color: Int = Color.WHITE) = consoleRepository.writeToConsole(input, color)
}