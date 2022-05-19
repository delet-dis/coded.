package com.hits.coded.domain.useCases.interpreterCaller

import com.hits.coded.domain.repositories.InterpreterCallerRepository

class GetExecutionResultsUseCase(
    private val interpreterCallerRepository: InterpreterCallerRepository
) {
    fun getExecutionResult() = interpreterCallerRepository.executionResult
}