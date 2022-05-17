package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.useCases.interpreter.interpreters.*

data class InterpretBlocksUseCases (
    val interpretArrayBlockUseCase: InterpretArrayBlockUseCase,
    val interpretConditionUseCase: InterpretConditionUseCase,
    val interpretExpressionBlockUseCase: InterpretExpressionBlockUseCase,
    val interpretIfBlockUseCase: InterpretIfBlockUseCase,
    val interpretIOBlockUseCase: InterpretIOBlockUseCase,
    val interpretLoopBlockUseCase: InterpretLoopBlockUseCase,
    val interpretVariableBlockUseCase: InterpretVariableBlockUseCase
)