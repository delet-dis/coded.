package com.hits.coded.data.implementations.bases.arrayBase

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType


class StringArray() : ArrayBase() {

    constructor(preparedArray: ArrayList<String>) : this() {
        for (value in preparedArray) {
            array.add(StoredVariable(null, VariableType.STRING, false, value))
        }
    }

    override fun parseString(inputString: String): ArrayBase {
        val parsedArray = ArrayList<String>()
        val pattern = Regex("(?:\"[\\S\\s]+?\")*")
        for (match in pattern.findAll(inputString)) {
            parsedArray.add(match.value.drop(1).dropLast(1))
        }

        return StringArray(parsedArray)
    }

    override fun push(value: Any) {
        if (value !is String)
            throw InterpreterException(
                interpreterUseCases.getCurrentBlockIdUseCase.getId(),
                ExceptionType.TYPE_MISMATCH
            )


        array.add(StoredVariable(null, VariableType.STRING, false, value))
    }
}