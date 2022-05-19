package com.hits.coded.domain.repositories.interpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase

abstract class InterpretExpressionBlockRepository {
    abstract suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase): Any
}