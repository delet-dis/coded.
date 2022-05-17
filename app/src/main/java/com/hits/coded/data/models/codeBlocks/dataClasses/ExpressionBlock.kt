package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType

data class ExpressionBlock(
    override var expressionBlockType: ExpressionBlockType? = null,
    override var leftSide: Any? = null,
    override var rightSide: Any? = null,
    override var id: Int? = null,
    override var nestedBlocks: List<BlockBase>? = null,
    override val type: BlockType = BlockType.EXPRESSION,
) : ExpressionBlockBase()
