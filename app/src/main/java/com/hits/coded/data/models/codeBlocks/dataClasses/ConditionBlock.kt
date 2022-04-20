package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.LogicalOperatorBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.MathematicalOperatorBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.ConditionBlockType

data class ConditionBlock(
    override val conditionBlockType: ConditionBlockType,
    override val type: BlockType,
    override val leftSide: Any,
    override val rightSide: Any?,
    override val logicalOperator: LogicalOperatorBase?,
    override val mathematicalOperator: MathematicalOperatorBase?,
    override val nestedBlocks: Array<BlockBase>? = null
) : ConditionBlockBase()