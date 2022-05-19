package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretConditionBlockRepository

class InterpretConditionUseCase(private val interpretConditionBlockRepository: InterpretConditionBlockRepository) {
    suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase) =
        interpretConditionBlockRepository.interpretConditionBlocks(conditionBlock)
}