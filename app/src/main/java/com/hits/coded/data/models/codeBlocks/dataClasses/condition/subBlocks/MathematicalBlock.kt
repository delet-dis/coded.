package com.hits.coded.data.models.codeBlocks.dataClasses.condition.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.MathematicalBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalBlockType

data class MathematicalBlock(override val mathematicalBlockType: MathematicalBlockType) :
    MathematicalBlockBase()