package com.hits.coded.domain.useCases.interpreter.helpers

import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterHelper

class SetCurrentIdVariableUseCase(private val interpreterHelper: InterpreterHelper) {
    fun setCurrentIdVariable(currentId: Int) =
        interpreterHelper.setCurrentIdVariable(currentId)
}