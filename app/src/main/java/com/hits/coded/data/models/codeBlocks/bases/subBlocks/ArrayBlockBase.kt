package com.hits.coded.data.models.codeBlocks.bases.subBlocks

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType

abstract class ArrayBlockBase : BlockBase() {
    abstract val arrayBlockType: ArrayBlockType
    abstract val arrayName: String?
    abstract val value: Any?
}