package com.hits.coded.domain.useCases.console

import com.hits.coded.domain.repositories.ConsoleRepository

class CheckIsInputAvailableUseCase(private val consoleRepository: ConsoleRepository) {
    fun checkIsInputAvailable() = consoleRepository.isInputAvailable
}