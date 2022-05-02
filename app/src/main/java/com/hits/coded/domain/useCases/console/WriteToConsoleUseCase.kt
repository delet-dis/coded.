package com.hits.coded.domain.useCases.console

import com.hits.coded.data.models.console.enums.ConsoleMessageType
import com.hits.coded.domain.repositories.ConsoleRepository

class WriteToConsoleUseCase(private val consoleRepository: ConsoleRepository) {
    fun writeInputToConsole(input: String) =
        consoleRepository.writeToConsole(input, ConsoleMessageType.INPUT)

    fun writeOutputToConsole(input: String) =
        consoleRepository.writeToConsole(input, ConsoleMessageType.OUTPUT)

    fun writeErrorToConsole(input: String) =
        consoleRepository.writeToConsole(input, ConsoleMessageType.ERROR)
}