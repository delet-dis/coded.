package com.hits.coded.data.implementations.bases.arrayBase

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

class IntArray() : ArrayBase() {

    constructor(preparedArray: ArrayList<Int>) : this() {
        for (value in preparedArray) {
            array.add(StoredVariable(null, VariableType.INT, false, value))
        }
    }

    override fun parseArray(inputString: String): ArrayBase {
        val parsedArray = ArrayList<Int>()
        val pattern = Regex("-?\\d+")
        for (match in pattern.findAll(inputString)) {
            parsedArray.add(match.value.toInt())
        }
        return IntArray(parsedArray)
    }

    override fun parseSingleValue(inputString: String): Any {
        val pattern = Regex("-?\\d+")
        val match = pattern.find(inputString)
            ?: throw InterpreterException(ExceptionType.INVALID_STRING)

        return match.value.toInt()
    }

    override fun push(value: Any?) {
        var newElement = value
        if (newElement is String)
            newElement = parseSingleValue(newElement)

        if (newElement !is Int)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        array.add(StoredVariable(null, VariableType.INT, false, newElement))
    }
}