package com.hits.coded.data.models.types

import com.hits.coded.R

enum class VariableType(val typeAsStringResource: Int) {
    STRING(R.string.stringType),
    DOUBLE(R.string.doubleType),
    INT(R.string.integerType),
    BOOLEAN(R.string.booleanType)
}