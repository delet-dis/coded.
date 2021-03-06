package com.hits.coded.domain.useCases.console

import com.hits.coded.domain.repositories.ConsoleRepository

class GetBufferUseCase(private val consoleRepository: ConsoleRepository) {
    fun getBuffer() = consoleRepository.output
}