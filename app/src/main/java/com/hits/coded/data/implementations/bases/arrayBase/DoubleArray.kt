package com.hits.coded.data.implementations.bases.arrayBase

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

class DoubleArray() : ArrayBase() {
    constructor(preparedArray: ArrayList<Double>) : this() {
        for (value in preparedArray) {
            array.add(StoredVariable(null, VariableType.DOUBLE, false, value))
        }
    }

    override fun parseString(inputString: String): ArrayBase {
        val parsedArray = ArrayList<Double>()
        val pattern = Regex("(?:-?\\d+(?:\\.\\d*)?)*")
        for (match in pattern.findAll(inputString)) {
            parsedArray.add(match.value.toDouble())
        }
        return DoubleArray(parsedArray)
    }

    override fun push(value: Any) {
        if (value !is Double)
            throw InterpreterException(
                interpreterUseCases.getCurrentBlockIdUseCase.getId(),
                ExceptionType.TYPE_MISMATCH
            )

        array.add(StoredVariable(null, VariableType.DOUBLE, false, value))
    }
}