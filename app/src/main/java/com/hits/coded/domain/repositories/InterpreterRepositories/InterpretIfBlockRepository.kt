package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase

abstract class InterpretIfBlockRepository {
    abstract suspend fun interpretIfBlock(ifBlock: IfBlockBase)
}