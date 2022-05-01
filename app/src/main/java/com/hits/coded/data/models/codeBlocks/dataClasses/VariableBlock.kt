package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable

data class VariableBlock(
    override val variableBlockType: VariableBlockType? = null,
    override var variableParams: StoredVariable? = null,
    override var id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null,
    override var valueToSet: Any? = null,
    override val type: BlockType = BlockType.VARIABLE,
) : VariableBlockBase()
