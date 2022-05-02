package com.hits.coded.domain.repositories

import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import kotlinx.coroutines.flow.Flow

abstract class InterpreterCallerRepository {
    abstract val executionResult: Flow<InterpreterException?>
    abstract suspend fun callInterpreter(start: StartBlock)
}