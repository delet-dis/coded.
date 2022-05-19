package com.hits.coded.data.models.codeBlocks.dataClasses

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType

data class IfBlock(
    override var ifBlockType: IfBlockType,
    override var id: Int? = null,
    override val type: BlockType = BlockType.IF,
    override var nestedBlocks: List<BlockBase>? = null,
    override var conditionBlock: Any? = null,
    override var elseBlocks: List<BlockBase>? = null
) : IfBlockBase()