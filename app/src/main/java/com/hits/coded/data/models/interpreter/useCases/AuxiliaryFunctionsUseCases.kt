package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.GetBaseTypeUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.GetVariableUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.InterpretBlocksUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.IsVariableUseCase

data class AuxiliaryFunctionsUseCases (
    val getVariableUseCase: GetVariableUseCase,
    val isVariableUseCase: IsVariableUseCase,
    val getBaseTypeUseCase: GetBaseTypeUseCase,
    val interpreterUseCases: InterpreterUseCases,
    val interpretBlocksUseCase: InterpretBlocksUseCase
        )