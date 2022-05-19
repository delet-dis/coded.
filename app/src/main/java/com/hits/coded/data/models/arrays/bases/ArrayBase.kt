package com.hits.coded.data.models.arrays.bases

import com.hits.coded.data.implementations.bases.arrayBase.BooleanArray
import com.hits.coded.data.implementations.bases.arrayBase.DoubleArray
import com.hits.coded.data.implementations.bases.arrayBase.IntArray
import com.hits.coded.data.implementations.bases.arrayBase.MultiDimensionalArray
import com.hits.coded.data.implementations.bases.arrayBase.StringArray
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType

abstract class ArrayBase() {
    protected val array = ArrayList<StoredVariable>()

    abstract fun parseArray(inputString: String): ArrayBase
    abstract fun parseSingleValue(inputString: String): Any
    abstract fun push(value: Any?)

    val size: Int
        get() = array.size

    fun removeAt(index: Int) {
        if (index < 0 || index >= array.size) {
            throw InterpreterException(ExceptionType.ARRAY_OUT_OF_BOUNDS)
        }

        array.removeAt(index)
    }


    fun concat(other: ArrayBase?) {
        if (other == null) {
            throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
        }

        if (array::class != other::class) {
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)
        }

        for (i in other.array) {
            this.array.add(i)
        }
    }


    operator fun get(index: Int): StoredVariable {
        if (index < 0 || index >= array.size)
            throw InterpreterException(ExceptionType.ARRAY_OUT_OF_BOUNDS)

        return array[index]
    }


    companion object {
        fun constructByType(variableType: VariableType): ArrayBase {
            return when (variableType) {
                VariableType.INT -> IntArray()
                VariableType.STRING -> StringArray()
                VariableType.DOUBLE -> DoubleArray()
                VariableType.BOOLEAN -> BooleanArray()
                VariableType.ARRAY -> MultiDimensionalArray()
            }
        }
    }

}