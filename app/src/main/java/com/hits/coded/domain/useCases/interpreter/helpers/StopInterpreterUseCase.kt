package com.hits.coded.domain.useCases.interpreter.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterManager

class StopInterpreterUseCase(private val interpreterManager: InterpreterManager) {
    fun stopInterpreter() = interpreterManager.stopInterpreter()
}