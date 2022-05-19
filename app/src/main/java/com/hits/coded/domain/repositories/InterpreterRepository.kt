package com.hits.coded.domain.repositories

import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException

abstract class InterpreterRepository {
    abstract val currentId: Int

    @Throws(InterpreterException::class)
    abstract suspend fun interpretStartBlock(startBlock: StartBlock)
}
