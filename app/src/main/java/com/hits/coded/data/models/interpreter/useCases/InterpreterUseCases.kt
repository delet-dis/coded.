package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.useCases.interpreter.InterpretConditionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretExpressionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretIOBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretLoopBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretVariableBlockUseCase

data class InterpreterUseCases(
    val interpretConditionBlockUseCase: InterpretConditionBlockUseCase,
    val interpretExpressionBlockUseCase: InterpretExpressionBlockUseCase,
    val interpretIOBlockUseCase: InterpretIOBlockUseCase,
    val interpretLoopBlockUseCase: InterpretLoopBlockUseCase,
    val interpretVariableBlockUseCase: InterpretVariableBlockUseCase
)