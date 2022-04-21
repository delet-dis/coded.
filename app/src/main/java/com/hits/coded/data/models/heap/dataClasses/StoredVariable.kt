package com.hits.coded.data.models.heap.dataClasses

import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType

data class StoredVariable(
    val type: VariableType,
    val isArray: Boolean,
    val value: Any
)
