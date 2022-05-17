package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase

abstract class InterpretLoopBlockRepository {
    abstract suspend fun interpretLoopBlock(loopBlock: LoopBlockBase)
}