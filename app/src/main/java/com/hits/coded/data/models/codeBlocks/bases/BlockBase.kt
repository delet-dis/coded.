package com.hits.coded.data.models.codeBlocks.bases

import com.hits.coded.data.models.codeBlocks.types.BlockType

abstract class BlockBase {
    abstract var id: Int?
    abstract val type: BlockType
    abstract var nestedBlocks: List<BlockBase>?
}