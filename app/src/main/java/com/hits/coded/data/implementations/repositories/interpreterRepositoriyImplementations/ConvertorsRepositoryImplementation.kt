package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.InterpreterRepositories.AuxiliaryFunctionsRepository
import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConvertorsRepositoryImplementation
@Inject constructor(
    private val heapUseCases: HeapUseCases,
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases
) : ConvertorsRepository() {
    override suspend fun convertAnyToDouble(value: Any?): Double {
        val processedValue = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(value, true)
        if (processedValue !is Number)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue.toDouble()
    }

    override suspend fun convertAnyToInt(value: Any?): Int {
        val processedValue = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(value, true)
        if (processedValue !is Number)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue.toInt()
    }

    override suspend fun convertAnyToBoolean(value: Any?): Boolean {
        val processedValue = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(value, true)
        if (processedValue !is Boolean)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue
    }

    override suspend fun convertAnyToString(value: Any): String {
        val processedValue = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(value, true)
        if (processedValue !is String)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue
    }

    override suspend fun convertAnyToStringIndulgently(value: Any): String =
        when (val processedValue =
            auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(value, true)) {
            is ArrayBase -> {
                var resultString = "[ "
                for (i in 0 until processedValue.size)
                    resultString += convertAnyToStringIndulgently(processedValue[i]) + ' '
                resultString += " ]"

                resultString
            }

            is String -> processedValue
            else -> processedValue.toString()
        }

    override suspend fun convertAnyToArrayBase(value: Any, array: ArrayBase): ArrayBase {
        var canBeStringField = true
        val processedValue: Any
        when (value) {
            is BlockBase -> {
                processedValue =
                    auxiliaryFunctionsUseCases.interpretBlocksUseCase.interpretBlock(value)
                        ?: throw InterpreterException(
                            ExceptionType.INVALID_BLOCK
                        )
                canBeStringField = false
            }
            else -> processedValue = value
        }

        when (processedValue) {
            is StoredVariable -> {
                return processedValue.value as? ArrayBase
                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }

            is String -> {
                return if (canBeStringField &&
                    (auxiliaryFunctionsUseCases.isVariableUseCase.isVariable(processedValue)) &&
                    heapUseCases.isVariableDeclaredUseCase.isVariableDeclared(processedValue)
                ) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(processedValue)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    convertAnyToArrayBase(foundedStoredVariable, array)
                } else {
                    array.parseArray(processedValue)
                }
            }

            else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
        }
    }

}