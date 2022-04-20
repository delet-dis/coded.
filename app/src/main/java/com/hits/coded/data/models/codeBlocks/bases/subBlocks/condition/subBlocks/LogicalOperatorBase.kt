package com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalOperatorType

abstract class LogicalOperatorBase: ConditionBlockBase() {
    abstract val logicalOperatorType: LogicalOperatorType
}