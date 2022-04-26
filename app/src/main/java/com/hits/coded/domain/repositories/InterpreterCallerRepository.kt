package com.hits.coded.domain.repositories

import com.hits.coded.data.models.interpreterException.InterpreterException
import kotlinx.coroutines.flow.Flow

abstract class InterpreterCallerRepository {
    abstract val executionResult: Flow<InterpreterException?>
    abstract suspend fun callInterpreter()
}