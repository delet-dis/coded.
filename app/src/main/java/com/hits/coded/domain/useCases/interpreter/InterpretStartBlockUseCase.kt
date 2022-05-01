package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.domain.repositories.InterpreterRepository
class InterpretStartBlockUseCase(
    private val interpreterRepository: InterpreterRepository
)  {
    suspend fun interpretStartBlock(start:StartBlock)=interpreterRepository.interpreteStartBlock(start)
}