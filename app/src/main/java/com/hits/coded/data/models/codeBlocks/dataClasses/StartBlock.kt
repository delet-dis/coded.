package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType

data class StartBlock(
    override val type: BlockType = BlockType.START,
    override val nestedBlocks: Array<BlockBase>?
) : BlockBase()