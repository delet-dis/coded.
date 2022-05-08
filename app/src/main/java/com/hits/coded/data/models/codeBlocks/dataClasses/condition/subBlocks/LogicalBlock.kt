package com.hits.coded.data.models.codeBlocks.dataClasses.condition.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.LogicalBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalBlockType

data class LogicalBlock(override val logicalBlockType: LogicalBlockType) : LogicalBlockBase()