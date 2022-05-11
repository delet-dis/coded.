package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType

abstract class IfBlockBase : BlockBase() {
    abstract val ifBlockType: IfBlockType
    abstract val conditionBlock: Any?
    abstract val elseBlocks: Array<BlockBase>?
}