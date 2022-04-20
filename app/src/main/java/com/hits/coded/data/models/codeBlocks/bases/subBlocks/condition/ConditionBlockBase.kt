package com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.LogicalOperatorBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.MathematicalOperatorBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.ConditionBlockType

abstract class ConditionBlockBase : BlockBase() {
    abstract val conditionBlockType: ConditionBlockType
    abstract val logicalOperator: LogicalOperatorBase?
    abstract val mathematicalOperator: MathematicalOperatorBase?
    abstract val leftSide: Any
    abstract val rightSide: Any?
}