package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase

abstract class InterpretConditionRepository {
abstract suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase):Boolean
}