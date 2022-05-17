package com.hits.coded.domain.repositories.InterpreterRepositories

import com.hits.coded.data.models.arrays.bases.ArrayBase

abstract class ConvertorsRepository {
    abstract suspend fun convertAnyToDouble(value: Any?):Double
    abstract suspend fun convertAnyToInt(value: Any?): Int
    abstract suspend fun convertAnyToBoolean(value: Any?): Boolean
    abstract suspend fun convertAnyToString(value: Any): String
    abstract suspend fun convertAnyToStringIndulgently(value: Any): String
    abstract suspend fun convertAnyToArrayBase(value: Any, array: ArrayBase): ArrayBase
}