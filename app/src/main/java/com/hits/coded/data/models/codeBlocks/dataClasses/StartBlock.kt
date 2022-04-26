package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType

data class StartBlock(
    override val type: BlockType = BlockType.START,
    override val id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null
) : BlockBase()