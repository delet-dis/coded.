package com.hits.coded.domain.useCases.console

import com.hits.coded.domain.repositories.ConsoleRepository

class FlushUseCase(private val consoleRepository: ConsoleRepository) {
    fun flush() = consoleRepository.flush()
}