package com.hits.coded.domain.useCases.console

import com.hits.coded.domain.repositories.ConsoleRepository

class WriteToConsoleUseCase(private val consoleRepository: ConsoleRepository) {
    fun writeToConsole(input: String) = consoleRepository.writeToConsole(input)
}