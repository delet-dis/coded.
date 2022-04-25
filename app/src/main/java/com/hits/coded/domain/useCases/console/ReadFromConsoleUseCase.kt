package com.hits.coded.domain.useCases.console

import com.hits.coded.domain.repositories.ConsoleRepository

class ReadFromConsoleUseCase(private val consoleRepository: ConsoleRepository) {
    suspend fun readFromConsole() = consoleRepository.readFromConsole()
}