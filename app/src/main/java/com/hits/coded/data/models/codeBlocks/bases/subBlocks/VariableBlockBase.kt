package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType

abstract class VariableBlockBase : BlockBase() {
    abstract val variableBlockType: VariableBlockType?
    abstract val variableParams: Any? // String and ArrayBlock
    abstract val valueToSet: Any?
}