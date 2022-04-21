package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreteVariableBlockUseCase(private val interpreterRepository: InterpreterRepository) {
    fun interpreteVariableBlock(variable:VariableBlock)=interpreterRepository.InterpreteVariableBlocks(variable)
}