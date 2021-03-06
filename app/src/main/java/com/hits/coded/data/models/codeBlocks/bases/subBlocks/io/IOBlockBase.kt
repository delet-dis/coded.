package com.hits.coded.data.models.codeBlocks.bases.subBlocks.io

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType

abstract class IOBlockBase : BlockBase() {
    abstract val ioBlockType: IOBlockType
    abstract val argument: Any?
}