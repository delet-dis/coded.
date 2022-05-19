package com.hits.coded.domain.repositories.interpreterRepositories.helpers

abstract class InterpreterHelper {
    var currentId = 0

    abstract fun getCurrentIdVariable(): Int

    abstract fun setCurrentIdVariable(currentId: Int)
}