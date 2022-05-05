package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType

data class ArrayBlock(
    override val type: BlockType = BlockType.ARRAY,
    override val arrayBlockType: ArrayBlockType,
    override val arrayName: String? = null,
    override val value: Any? = null,
    override var id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null
) : ArrayBlockBase()
