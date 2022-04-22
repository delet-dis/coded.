package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.types.VariableType

data class VariableBlock(
    override val variableBlockType: VariableBlockType,
    override val variableParams: StoredVariable,
    override val type: BlockType = BlockType.VARIABLE,
    override val nestedBlocks: Array<BlockBase>? = null,
    override val variableToChange: StoredVariable? = null
) : VariableBlockBase()
