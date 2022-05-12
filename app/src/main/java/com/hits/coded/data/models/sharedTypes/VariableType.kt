package com.hits.coded.data.models.sharedTypes

import com.hits.coded.R

enum class VariableType(val typeAsStringResource: Int) {
    STRING(R.string.stringType),
    DOUBLE(R.string.doubleType),
    INT(R.string.integerType),
    BOOLEAN(R.string.booleanType),
    ARRAY(R.string.multidimensionalArrayType)
}