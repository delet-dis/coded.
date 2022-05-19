package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretVariableBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretVariableBlockRepositoryImplementation
@Inject constructor(
    private val interpreterConverterUseCases: InterpreterConverterUseCases,
    private val interpreterAuxiliaryUseCases: InterpreterAuxiliaryUseCases,
    private val heapUseCases: HeapUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases
) : InterpretVariableBlockRepository(){
@Throws(InterpreterException::class)
    override suspend fun interpretVariableBlocks(variable: VariableBlockBase) {
        variable.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        val variableParams: Any = variable.variableParams
            ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_SET -> {
                val valueToSet: Any = variable.valueToSet
                    ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                val currentStoredVariable =
                    interpreterAuxiliaryUseCases.getVariableUseCase.getVariable(variableParams)
                currentStoredVariable.value = when (currentStoredVariable.type) {
                    VariableType.STRING -> interpreterConverterUseCases.convertAnyToStringUseCase.convertAnyToString(
                        valueToSet
                    )
                    VariableType.INT -> interpreterConverterUseCases.convertAnyToIntUseCase.convertAnyToInt(
                        valueToSet
                    )
                    VariableType.DOUBLE -> interpreterConverterUseCases.convertAnyToDoubleUseCase.convertAnyToDouble(
                        valueToSet
                    )
                    VariableType.BOOLEAN -> interpreterConverterUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(
                        valueToSet
                    )
                    VariableType.ARRAY -> interpreterConverterUseCases.convertAnyToArrayBaseUseCase.convertAnyToArrayBase(
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
                        interpreterAuxiliaryUseCases.getVariableUseCase.getVariable(variableParams)

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