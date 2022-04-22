package com.hits.coded.domain.repositories

import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock

abstract class ExecutorRepository {
    abstract fun ExecuteLoop(loopBlock: LoopBlock)
    abstract fun Execute
}