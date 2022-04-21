package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.ConditionBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreteConditionBlockUseCase(
    private val interpriterRepository: InterpreterRepository
) {
    fun interpreteConditionBlock(condition: ConditionBlock) =
        interpriterRepository.InterpreteConditionBlocks(condition)
}