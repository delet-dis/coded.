package com.hits.coded.domain.repositories.interpreterRepositories.helpers

abstract class InterpreterManager {
    var isInterpreting: Boolean = true
    abstract fun stopInterpreter()
    abstract fun startInterpreter()
}