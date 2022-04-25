package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.LoopBlockType

data class LoopBlock(
    override val loopBlockType: LoopBlockType,
    override val conditionBlock: ConditionBlockBase,
    override val type: BlockType = BlockType.LOOP,
    override val nestedBlocks: Array<BlockBase>? = null,
) : LoopBlockBase()
