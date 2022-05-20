package com.hits.coded.data.implementations.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpreterManagerImplementation
@Inject constructor() : InterpreterManager() {
    override fun stopInterpreter() {
        this.isInterpreting = false
    }

    override fun startInterpreter() {
        this.isInterpreting = true
    }
}