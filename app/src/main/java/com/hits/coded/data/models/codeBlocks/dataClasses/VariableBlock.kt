package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.sharedTypes.VariableType

data class VariableBlock(
    override val variableBlockType: VariableBlockType,
    override val id: Int? = null,
    override var variableName: String? = null,
    override val variableType: VariableType? = null,
    override val isArray: Boolean? = null,
    override val variableValue: Any? = null,
    override val type: BlockType = BlockType.VARIABLE,
    override var nestedBlocks: Array<BlockBase>? = null,
) : VariableBlockBase()
