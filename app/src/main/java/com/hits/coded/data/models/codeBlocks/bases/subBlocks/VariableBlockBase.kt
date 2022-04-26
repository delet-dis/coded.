package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class VariableBlockBase : BlockBase() {
    abstract val variableBlockType: VariableBlockType?
    abstract val variableParams: StoredVariable?
    abstract val variableToChange: StoredVariable?
}