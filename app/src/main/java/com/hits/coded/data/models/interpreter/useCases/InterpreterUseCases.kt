package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.useCases.interpreter.GetCurrentBlockIdUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretStartBlockUseCase

data class InterpreterUseCases(
    val interpretStartBlock: InterpretStartBlockUseCase,
    val getCurrentBlockIdUseCase: GetCurrentBlockIdUseCase

)