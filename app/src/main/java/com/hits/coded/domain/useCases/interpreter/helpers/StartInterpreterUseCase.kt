package com.hits.coded.domain.useCases.interpreter.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterManager

class StartInterpreterUseCase(private val interpreterManager: InterpreterManager) {
    fun startInterpreter() = interpreterManager.startInterpreter()
}