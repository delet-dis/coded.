package com.hits.coded.data.models.interpreter.arrays.arrayImplementations

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreter.arrays.ArrayBase
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType

class MultiDimensionalArray(): ArrayBase() {
    override fun parseString(inputString: String): ArrayBase {
        TODO("Not yet implemented")
    }

    override fun push(value: Any) {
        if (value !is ArrayBase)
            throw InterpreterException(
                interpreterUseCases.getCurrentBlockIdUseCase.getId(),
                ExceptionType.TYPE_MISMATCH
            )

        //TODO: array type
        array.add(StoredVariable(null, null, true, value))
    }
}