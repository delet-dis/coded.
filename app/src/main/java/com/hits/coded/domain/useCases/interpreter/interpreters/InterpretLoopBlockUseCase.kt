package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretLoopBlockRepository

class InterpretLoopBlockUseCase(private val interpretLoopBlockRepository: InterpretLoopBlockRepository) {
    suspend fun interpretLoopBlocks(loopBlock: LoopBlockBase)=interpretLoopBlockRepository.interpretLoopBlock(loopBlock)
}