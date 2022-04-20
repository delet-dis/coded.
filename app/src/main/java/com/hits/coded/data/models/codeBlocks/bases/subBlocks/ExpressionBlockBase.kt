package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType

abstract class ExpressionBlockBase : BlockBase() {
    abstract val expressionBlockType: ExpressionBlockType
    abstract val leftSide: Any?
    abstract val rightSide: Any?
}