package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.sharedTypes.VariableType

abstract class VariableBlockBase : BlockBase() {
    abstract val variableBlockType: VariableBlockType
    abstract var variableName: String?
    abstract val variableType: VariableType?
    abstract val variableValue: Any?
    abstract val isArray: Boolean?
}