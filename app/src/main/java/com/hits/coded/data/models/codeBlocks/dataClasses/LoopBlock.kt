package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.LoopBlockType

data class LoopBlock(
    override var loopBlockType: LoopBlockType? = null,
    override var conditionBlock: Any? = null,
    override var id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null,
    override val type: BlockType = BlockType.LOOP,
) : LoopBlockBase()
