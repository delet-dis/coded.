package com.hits.coded.data.models.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.domain.useCases.interpreter.*

data class InterpreterUseCases(
    val interpreteStartBlockUseCase:InterpretStartBlockUseCase
)