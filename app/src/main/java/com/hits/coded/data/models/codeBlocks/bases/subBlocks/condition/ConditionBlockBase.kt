package com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.subBlocks.LogicalBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.subBlocks.MathematicalBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.ConditionBlockType

abstract class ConditionBlockBase : BlockBase() {
    abstract val conditionBlockType: ConditionBlockType?
    abstract val logicalBlock: LogicalBlock?
    abstract val mathematicalBlock: MathematicalBlock?
    abstract val leftSide: Any?
    abstract val rightSide: Any?
}