package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class AuxiliaryFunctionsRepository {
    abstract suspend fun getVariable(variableIdentifier: Any): StoredVariable
    abstract fun isVariable(value: String): Boolean
    abstract suspend fun getBaseType(value: Any?, canBeStringField: Boolean = true): Any
    abstract suspend fun interpretBlock(block: BlockBase): Any?

}