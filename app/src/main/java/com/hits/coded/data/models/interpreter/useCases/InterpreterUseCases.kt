package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.repositories.InterpreterRepositories.*
import com.hits.coded.domain.useCases.interpreter.GetCurrentBlockIdUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretStartBlockUseCase
import com.hits.coded.domain.useCases.interpreter.interpreters.*

data class InterpreterUseCases(
    val interpretStartBlock: InterpretStartBlockUseCase,
    val getCurrentBlockIdUseCase: GetCurrentBlockIdUseCase

)