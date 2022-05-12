package com.hits.coded.data.implementations.bases.arrayBase

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

class DoubleArray() : ArrayBase() {

    constructor(preparedArray: ArrayList<Double>) : this() {
        for (value in preparedArray) {
            array.add(StoredVariable(null, VariableType.DOUBLE, false, value))
        }
    }

    override fun parseArray(inputString: String): ArrayBase {
        val parsedArray = ArrayList<Double>()
        val pattern = Regex("-?\\d+(?:\\.\\d*)?")
        for (match in pattern.findAll(inputString)) {
            parsedArray.add(match.value.toDouble())
        }
        return DoubleArray(parsedArray)
    }

    override fun parseSingleValue(inputString: String): Any {
        val pattern = Regex("-?\\d+(?:\\.\\d*)?")
        val match = pattern.find(inputString)
            ?: throw InterpreterException(ExceptionType.INVALID_STRING)

        return match.value.toDouble()
    }

    override fun push(value: Any?) {
        var newElement = value
        if (newElement is String)
            newElement = parseSingleValue(newElement)

        if (newElement !is Double)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        array.add(StoredVariable(null, VariableType.DOUBLE, false, newElement))
    }
}