package com.hits.coded.data.models.codeBlocks.bases

import android.graphics.Color
import com.hits.coded.data.models.codeBlocks.types.BlockType

abstract class BlockBase {
    abstract val type: BlockType
    abstract val color: Color
    abstract val nestedBlocks: Array<BlockBase>?
}