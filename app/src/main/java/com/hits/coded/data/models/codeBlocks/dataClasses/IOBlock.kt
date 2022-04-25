package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType

data class IOBlock(
    override val ioBlockType: IOBlockType,
    override val variableBlock: VariableBlockBase,
    override val type: BlockType = BlockType.IO,
    override val nestedBlocks: Array<BlockBase>? = null,
) : IOBlockBase()