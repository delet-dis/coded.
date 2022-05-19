package com.hits.coded.data.implementations.bases.arrayBase

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

class MultiDimensionalArray() : ArrayBase() {

    override fun parseArray(inputString: String): ArrayBase {
        val parsedArray = MultiDimensionalArray()
        var arrayFound = false
        var openedBrackets = 0
        var arrayString = ""

        for (char in inputString) {
            if (char == '[') {
                arrayFound = true
                openedBrackets++
            } else if (char == ']') {
                openedBrackets--
            } else if (arrayFound) {
                arrayString += char
            }


            if (arrayFound && openedBrackets == 0) {
                arrayFound = false
                var nestedArray = constructByType(deductType(arrayString))
                nestedArray = nestedArray.parseArray(arrayString)
                parsedArray.push(nestedArray)
                arrayString = ""
            }
        }

        return parsedArray
    }

    private fun deductType(arrayString: String): VariableType {
        var type = VariableType.BOOLEAN
        var isPrevDigit = false
        for (char in arrayString) {
            if (char == '[')
                return VariableType.ARRAY

            if (char == '"')
                return VariableType.STRING
            else if (char.isDigit()) {
                isPrevDigit = true
                if (char >= '2' || char <= '9') {
                    type = VariableType.INT
                }
            } else if (isPrevDigit && char == '.')
                type = VariableType.DOUBLE
        }

        return type
    }

    override fun parseSingleValue(inputString: String): Any = parseArray(inputString)

    override fun push(value: Any?) {
        var newElement = value
        if (newElement is String)
            newElement = parseSingleValue(newElement)

        if (newElement !is ArrayBase)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)


        array.add(StoredVariable(null, VariableType.ARRAY, true, newElement))
    }
}