package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpretLoopBlockUseCase(
    private val interpreterRepository: InterpreterRepository
    ) {
    suspend fun interpreteLoopBlock(loop:LoopBlock)=interpreterRepository.interpretLoopBlocks(loop)
}