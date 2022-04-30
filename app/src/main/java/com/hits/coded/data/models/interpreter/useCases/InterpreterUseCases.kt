package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.useCases.interpreter.InterpreteConditionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpreteExpressionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpreteIOBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpreteLoopBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpreteVariableBlockUseCase

data class InterpreterUseCases(
    val interpreteConditionBlockUseCase: InterpreteConditionBlockUseCase,
    val interpreteExpressionBlockUseCase: InterpreteExpressionBlockUseCase,
    val interpreteIOBlockUseCase: InterpreteIOBlockUseCase,
    val interpreteLoopBlockUseCase: InterpreteLoopBlockUseCase,
    val interpreteVariableBlockUseCase: InterpreteVariableBlockUseCase
)