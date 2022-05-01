package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType

data class IOBlock(
    override val ioBlockType: IOBlockType,
    override var argument: Any? = null,
    override var id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null,
    override val type: BlockType = BlockType.IO,
) : IOBlockBase()