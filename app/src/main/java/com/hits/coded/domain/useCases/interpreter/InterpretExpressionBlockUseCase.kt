package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpretExpressionBlockUseCase(
    private val interpreterRepository: InterpreterRepository
    ) {
    suspend fun interpreteExpressionBlock(expression:ExpressionBlock)=interpreterRepository.interpretExpressionBlocks(expression)
}