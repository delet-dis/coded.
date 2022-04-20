package com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.ConditionBlockType

abstract class ConditionBlockBase: BlockBase() {
    abstract val conditionBlocType:ConditionBlockType
}