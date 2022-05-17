package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase

abstract class InterpretIOBlockRepository {
    abstract suspend fun interpretIOBlocks(IO: IOBlockBase): String?
}