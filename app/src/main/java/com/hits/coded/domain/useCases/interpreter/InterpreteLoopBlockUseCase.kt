package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreteLoopBlockUseCase(
    private val interpreterRepository: InterpreterRepository
    ) {
    fun interpreteLoopBlock(loop:LoopBlock)=interpreterRepository.interpreteLoopBlocks(loop)
}