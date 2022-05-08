package com.hits.coded.data.models.codeBlocks.dataClasses.condition

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.subBlocks.LogicalBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.subBlocks.MathematicalBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.ConditionBlockType

data class ConditionBlock(
    override var conditionBlockType: ConditionBlockType? = null,
    override var leftSide: Any? = null,
    override var rightSide: Any? = null,
    override var logicalBlock: LogicalBlock? = null,
    override var mathematicalBlock: MathematicalBlock? = null,
    override var id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null,
    override val type: BlockType = BlockType.CONDITION,
) : ConditionBlockBase()