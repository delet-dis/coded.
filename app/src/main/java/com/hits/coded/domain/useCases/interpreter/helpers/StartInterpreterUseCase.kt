package com.hits.coded.domain.useCases.interpreter.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.StopInterpreter

class StartInterpreterUseCase(private val stopInterpreter: StopInterpreter) {
    fun startInterpreter() = stopInterpreter.startInterpreter()
}