package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretConditionRepository

class InterpretConditionUseCase(private val interpretConditionRepository:InterpretConditionRepository) {
    suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase)=interpretConditionRepository.interpretConditionBlocks(conditionBlock)
}