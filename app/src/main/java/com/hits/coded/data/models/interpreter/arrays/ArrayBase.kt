package com.hits.coded.data.models.interpreter.arrays

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreter.useCases.InterpreterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import javax.inject.Inject

abstract class ArrayBase() {
    @Inject
    lateinit var interpreterUseCases: InterpreterUseCases
    protected val array = ArrayList<StoredVariable>()

    abstract fun parseString(inputString: String): ArrayBase
    abstract fun push(value: Any)

    fun pop(): StoredVariable {
        if(array.size == 0)
            throw InterpreterException(
                interpreterUseCases.getCurrentBlockIdUseCase.getId(),
                ExceptionType.ARRAY_OUT_OF_BOUNDS
            )

        return array.removeLast()
    }


    operator fun get(index: Int): StoredVariable {
        if (index < 0 || index >= array.size)
            throw InterpreterException(
                interpreterUseCases.getCurrentBlockIdUseCase.getId(),
                ExceptionType.ARRAY_OUT_OF_BOUNDS
            )

        return array[index]
    }

}