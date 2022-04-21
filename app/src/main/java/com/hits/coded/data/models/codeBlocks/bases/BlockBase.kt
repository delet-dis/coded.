package com.hits.coded.data.models.codeBlocks.bases

import com.hits.coded.data.models.codeBlocks.types.BlockType

abstract class BlockBase {
    abstract val type: BlockType
    abstract val nestedBlocks: Array<BlockBase>?
}