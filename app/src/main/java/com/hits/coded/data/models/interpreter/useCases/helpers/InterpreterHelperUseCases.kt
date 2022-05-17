package com.hits.coded.data.models.interpreter.useCases.helpers

import com.hits.coded.domain.useCases.interpreter.helpers.GetCurrentIdVariableUseCase
import com.hits.coded.domain.useCases.interpreter.helpers.SetCurrentIdVariableUseCase

data class InterpreterHelperUseCases(
    val getCurrentIdVariableUseCase: GetCurrentIdVariableUseCase,
    val setCurrentIdVariableUseCase: SetCurrentIdVariableUseCase
)