package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase

abstract class InterpretArrayBlockRepository {
    abstract suspend fun interpretArrayBlock(block: ArrayBlockBase): Any
}