package com.hits.coded.data.models.interpreter.useCases.repositories

import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretArrayBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretConditionUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretExpressionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretIOBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretIfBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretLoopBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.InterpretVariableBlockUseCase

data class InterpreterBlocksUseCases(
    val interpretArrayBlockUseCase: InterpretArrayBlockUseCase,
    val interpretConditionUseCase: InterpretConditionUseCase,
    val interpretExpressionBlockUseCase: InterpretExpressionBlockUseCase,
    val interpretIfBlockUseCase: InterpretIfBlockUseCase,
    val interpretIOBlockUseCase: InterpretIOBlockUseCase,
    val interpretLoopBlockUseCase: InterpretLoopBlockUseCase,
    val interpretVariableBlockUseCase: InterpretVariableBlockUseCase
)