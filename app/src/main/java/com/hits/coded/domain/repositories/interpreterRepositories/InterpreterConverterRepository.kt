package com.hits.coded.domain.repositories.interpreterRepositories

import com.hits.coded.data.models.arrays.bases.ArrayBase

abstract class InterpreterConverterRepository {
    abstract suspend fun convertAnyToDouble(value: Any?): Double
    abstract suspend fun convertAnyToInt(value: Any?): Int
    abstract suspend fun convertAnyToBoolean(value: Any?): Boolean
    abstract suspend fun convertAnyToString(value: Any): String
    abstract suspend fun convertAnyToStringIndulgently(value: Any): String
    abstract suspend fun convertAnyToArrayBase(value: Any, array: ArrayBase): ArrayBase
}