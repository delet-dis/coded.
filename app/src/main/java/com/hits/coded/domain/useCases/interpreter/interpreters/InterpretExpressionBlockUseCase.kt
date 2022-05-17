package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretExpressionBlockRepository

class InterpretExpressionBlockUseCase(private val interpretExpressionBlockRepository: InterpretExpressionBlockRepository) {
    suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase)=interpretExpressionBlockRepository.interpretExpressionBlocks(expressionBlock)
}