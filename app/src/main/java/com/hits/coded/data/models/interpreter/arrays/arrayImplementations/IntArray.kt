package com.hits.coded.data.models.interpreter.arrays.arrayImplementations

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreter.arrays.ArrayBase
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

class IntArray() : ArrayBase() {
    constructor(preparedArray: ArrayList<Int>) : this() {
        for (value in preparedArray) {
            array.add(StoredVariable(null, VariableType.INT, false, value))
        }
    }

    override fun parseString(inputString: String): ArrayBase {
        val parsedArray = ArrayList<Int>()
        val pattern = Regex("(?:-?\\d+)*")
        for (match in pattern.findAll(inputString)) {
            parsedArray.add(match.value.toInt())
        }
        return IntArray(parsedArray)
    }

    override fun push(value: Any) {
        if (value !is Int)
            throw InterpreterException(
                interpreterUseCases.getCurrentBlockIdUseCase.getId(),
                ExceptionType.TYPE_MISMATCH
            )

        array.add(StoredVariable(null, VariableType.INT, false, value))
    }
}