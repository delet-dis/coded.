package com.hits.coded.data.models.heap.dataClasses

import com.hits.coded.data.models.sharedTypes.VariableType

data class StoredVariable(
    val name: String,
    val type: VariableType,
    val isArray: Boolean,
    val value: Any
)
