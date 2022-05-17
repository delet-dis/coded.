package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase

abstract class InterpretVariableBlockRepository {
    abstract suspend fun interpretVariableBlocks(variable: VariableBlockBase)
}