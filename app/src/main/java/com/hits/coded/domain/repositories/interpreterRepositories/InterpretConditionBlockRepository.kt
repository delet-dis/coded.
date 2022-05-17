package com.hits.coded.domain.repositories.interpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase

abstract class InterpretConditionBlockRepository {
    abstract suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase): Boolean
}