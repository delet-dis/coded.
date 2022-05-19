package com.hits.coded.domain.repositories.interpreterRepositories.helpers

abstract class StopInterpreter {
    var isInterpreting:Boolean = true
    abstract fun stopInterpreter()
}