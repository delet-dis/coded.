package com.hits.coded.data.implementations.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpreterHelperImplementation @Inject constructor() : InterpreterHelper() {
    override fun getCurrentIdVariable() = currentId

    override fun setCurrentIdVariable(currentId: Int) {
        this.currentId = currentId
    }
}