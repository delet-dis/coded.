package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType

data class VariableBlock(
    override val variableBlockType: VariableBlockType,
    override val variableName: String,
    override val variableType: VariableType,
    override val isArray: Boolean,
    override val variableValue: Any,
    override val type: BlockType = BlockType.VARIABLE,
    override val nestedBlocks: Array<BlockBase>? = null,
) : VariableBlockBase()
