package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreteVariableBlockUseCase(private val interpreterRepository: InterpreterRepository) {
    suspend fun interpreteVariableBlock(variable:VariableBlock)=interpreterRepository.interpreteVariableBlocks(variable)
}