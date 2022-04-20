package com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalOperatorType

abstract class MathematicalOperatorBase: ConditionBlockBase(){
    abstract val mathematicalOperatorType: MathematicalOperatorType
}