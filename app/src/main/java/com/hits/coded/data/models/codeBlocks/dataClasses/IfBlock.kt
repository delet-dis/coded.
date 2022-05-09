package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType

data class IfBlock(
    override val ifBlockType: IfBlockType,
    override var id: Int?=null,
    override val type: BlockType=BlockType.IF,
    override var nestedBlocks: Array<BlockBase>?=null,
    override val conditionBlock: ConditionBlockBase?,
    override val elseBlocks: Array<BlockBase>?=null
): IfBlockBase()