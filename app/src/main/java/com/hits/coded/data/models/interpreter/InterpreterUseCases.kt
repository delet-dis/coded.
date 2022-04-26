package com.hits.coded.data.models.interpreter

import com.hits.coded.domain.useCases.interpreter.*

data class InterpreterUseCases(
    val interpreteConditionBlockUseCase: InterpreteConditionBlockUseCase,
    val interpreteExpressionBlockUseCase: InterpreteExpressionBlockUseCase,
    val interpreteIOBlockUseCase: InterpreteIOBlockUseCase,
    val interpreteLoopBlockUseCase: InterpreteLoopBlockUseCase,
    val interpreteVariableBlockUseCase: InterpreteVariableBlockUseCase
)