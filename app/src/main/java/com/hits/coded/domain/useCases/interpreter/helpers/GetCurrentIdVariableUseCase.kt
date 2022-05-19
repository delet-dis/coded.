package com.hits.coded.domain.useCases.interpreter.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterHelper

class GetCurrentIdVariableUseCase(private val interpreterHelper: InterpreterHelper) {
    fun getCurrentIdVariable() = interpreterHelper.getCurrentIdVariable()
}