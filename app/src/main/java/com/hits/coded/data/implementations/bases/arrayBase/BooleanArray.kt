package com.hits.coded.data.implementations.bases.arrayBase

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

class BooleanArray() : ArrayBase() {

    constructor(preparedArray: ArrayList<Boolean>) : this() {
        for (value in preparedArray) {
            array.add(StoredVariable(null, VariableType.BOOLEAN, false, value))
        }
    }

    override fun parseArray(inputString: String): ArrayBase {
        val parsedArray = ArrayList<Boolean>()
        val pattern = Regex("[10]|(?:true|false)")
        for (match in pattern.findAll(inputString)) {
            if (match.value[0] == '1' || match.value[0] == 't')
                parsedArray.add(true)
            else
                parsedArray.add(false)
        }
        return BooleanArray(parsedArray)
    }

    override fun parseSingleValue(inputString: String): Any {
        val pattern = Regex("[10]|(?:true|false)")
        val match = pattern.find(inputString)
            ?: throw InterpreterException(ExceptionType.INVALID_STRING)

        if (match.value[0] == '1' || match.value[0] == 't')
            return true
        return false
    }

    override fun push(value: Any?) {
        var newElement = value
        if (newElement is String)
            newElement = parseSingleValue(newElement)

        if (newElement !is Boolean)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        array.add(StoredVariable(null, VariableType.BOOLEAN, false, newElement))
    }
}