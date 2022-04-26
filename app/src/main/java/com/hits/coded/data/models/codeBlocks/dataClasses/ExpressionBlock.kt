package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType

data class ExpressionBlock(
    override val expressionBlockType: ExpressionBlockType,
    override val leftSide: Any,
    override val rightSide: Any,
    override val id: Int? = null,
    override var nestedBlocks: Array<BlockBase>? = null,
    override val type: BlockType = BlockType.EXPRESSION,
):ExpressionBlockBase()
