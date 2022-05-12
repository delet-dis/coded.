package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.LoopBlockType

abstract class LoopBlockBase : BlockBase() {
    abstract val loopBlockType: LoopBlockType
    abstract val conditionBlock: Any
}