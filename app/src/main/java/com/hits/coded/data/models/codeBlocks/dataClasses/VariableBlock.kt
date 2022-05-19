package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType

data class VariableBlock(
    override var variableBlockType: VariableBlockType? = null,
    override var variableParams: Any? = null,
    override var id: Int? = null,
    override var valueToSet: Any? = null,
    override var nestedBlocks: List<BlockBase>? = null,
    override val type: BlockType = BlockType.VARIABLE,
) : VariableBlockBase()
