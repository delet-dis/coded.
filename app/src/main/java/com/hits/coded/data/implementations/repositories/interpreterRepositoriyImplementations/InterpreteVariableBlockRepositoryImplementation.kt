package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreter.useCases.ConvertorsUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretVariableBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretVariableBlockRepositoryImplementation
@Inject constructor(
    private val convertorsUseCases: ConvertorsUseCases,
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases,
    private val heapUseCases: HeapUseCases,
    private val currentIdValue: CurrentIdValue
) : InterpretVariableBlockRepository() {
    override suspend fun interpretVariableBlocks(variable: VariableBlockBase) {
        variable.id?.let {
            currentIdValue.currentId = it
        }

        val variableParams: Any = variable.variableParams
            ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_SET -> {
                val valueToSet: Any = variable.valueToSet
                    ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                val currentStoredVariable =
                    auxiliaryFunctionsUseCases.getVariableUseCase.getVariable(variableParams)
                currentStoredVariable.value = when (currentStoredVariable.type) {
                    VariableType.STRING -> convertorsUseCases.convertAnyToStringUseCase.convertAnyToString(
                        valueToSet
                    )
                    VariableType.INT -> convertorsUseCases.convertAnyToIntUseCase.convertAnyToInt(
                        valueToSet
                    )
                    VariableType.DOUBLE -> convertorsUseCases.convertAnyToDoubleUseCase.convertAnyToDouble(
                        valueToSet
                    )
                    VariableType.BOOLEAN -> convertorsUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(
                        valueToSet
                    )
                    VariableType.ARRAY -> convertorsUseCases.convertAnyToArrayBaseUseCase.convertAnyToArrayBase(
                        valueToSet,
                        currentStoredVariable.value as ArrayBase
                    )
                    else -> throw InterpreterException(ExceptionType.WTF)
                }
            }

            VariableBlockType.VARIABLE_CHANGE -> {
                val valueToSet: Any = variable.valueToSet
                    ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                if (valueToSet is String) {
                    val operand = valueToSet[0]
                    val currentStoredVariable =
                        auxiliaryFunctionsUseCases.getVariableUseCase.getVariable(variableParams)

                    val origValue = currentStoredVariable.value
                    if (origValue !is Number)
                        throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                    val delta = valueToSet.drop(1).toDoubleOrNull()
                        ?: throw InterpreterException(
                            ExceptionType.INVALID_STRING
                        )

                    val result = when (operand) {
                        '+' -> origValue.toDouble() + delta
                        '-' -> origValue.toDouble() - delta
                        '*' -> origValue.toDouble() * delta
                        '/' -> origValue.toDouble() / delta
                        '%' -> origValue.toDouble() % delta
                        else -> throw InterpreterException(ExceptionType.WRONG_OPERAND_USE_CASE)
                    }

                    currentStoredVariable.value =
                        when (currentStoredVariable.value) {
                            is Int -> result.toInt()
                            is Double -> result
                            else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                        }
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }
            VariableBlockType.VARIABLE_CREATE -> {
                val currentVariable = variableParams as? StoredVariable
                    ?: throw InterpreterException(ExceptionType.WTF)

                val type = currentVariable.type
                    ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                if (currentVariable.isArray == true) {
                    currentVariable.value = ArrayBase.constructByType(type)
                } else {
                    currentVariable.value = when (type) {
                        VariableType.BOOLEAN -> false
                        VariableType.STRING -> ""
                        VariableType.DOUBLE -> 0.0
                        VariableType.INT -> 0
                        VariableType.ARRAY ->
                            throw InterpreterException(ExceptionType.WTF)
                    }
                }
                heapUseCases.addVariableUseCase.addVariable(currentVariable)
            }
            null -> throw InterpreterException(ExceptionType.WTF)
        }
    }
}