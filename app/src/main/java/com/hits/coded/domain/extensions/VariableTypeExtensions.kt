package com.hits.coded.domain.extensions

import com.hits.coded.data.models.sharedTypes.VariableType

fun findVariableType(typeAsString: String): VariableType {
    return VariableType.values().find {
        it.typeAsString == typeAsString
    } ?: VariableType.STRING
}