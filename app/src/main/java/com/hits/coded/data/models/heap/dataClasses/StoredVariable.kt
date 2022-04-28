package com.hits.coded.data.models.heap.dataClasses

import com.hits.coded.data.models.types.VariableType

data class StoredVariable(
    var name: String? = null,
    var type: VariableType? = null,
    var isArray: Boolean? = null,
    var value: Any? = null
)
