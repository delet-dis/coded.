package com.hits.coded.data.models.interpreter.useCases.repositories

import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.GetBaseTypeUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.GetVariableUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.InterpretAnyBlockUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.IsVariableUseCase

data class InterpreterAuxiliaryUseCases(
    val getVariableUseCase: GetVariableUseCase,
    val isVariableUseCase: IsVariableUseCase,
    val getBaseTypeUseCase: GetBaseTypeUseCase,
    val interpretAnyBlockUseCase: InterpretAnyBlockUseCase
)