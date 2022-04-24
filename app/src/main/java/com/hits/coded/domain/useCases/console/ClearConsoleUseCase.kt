package com.hits.coded.domain.useCases.console

import com.hits.coded.domain.repositories.ConsoleRepository

class ClearConsoleUseCase(private val consoleRepository: ConsoleRepository) {
    suspend fun clearConsole() = consoleRepository.clear()
}