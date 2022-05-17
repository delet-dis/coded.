package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.*
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.ConditionBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.InterpretBlocksUseCases
import com.hits.coded.data.models.interpreter.useCases.InterpreterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.InterpreterRepositories.AuxiliaryFunctionsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuxiliaryFunctionsRepositoryImplementation
@Inject constructor(
    private val heapUseCases: HeapUseCases,
    private val interpretBlocksUseCases: InterpretBlocksUseCases,
    private val interpreterUseCases: InterpreterUseCases
) : AuxiliaryFunctionsRepository() {
    override suspend fun getVariable(variableIdentifier: Any): StoredVariable {
        val currentVariable: StoredVariable
        when (variableIdentifier) {
            is String -> {
                currentVariable =
                    heapUseCases.getVariableUseCase.getVariable(variableIdentifier)
                        ?: throw InterpreterException(
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
            }
            is ArrayBlockBase -> {
                val value = interpretBlocksUseCases.interpretArrayBlockUseCase.interpretArrayBlock(
                    variableIdentifier
                )
                if (value !is StoredVariable)
                    throw InterpreterException(ExceptionType.INVALID_BLOCK)

                currentVariable = value
            }

            is StoredVariable -> {
                if (variableIdentifier.name == null)
                    throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                currentVariable =
                    heapUseCases.getVariableUseCase.getVariable(variableIdentifier.name!!)
                        ?: throw InterpreterException(
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
            }

            else -> throw InterpreterException(ExceptionType.INVALID_BLOCK)
        }

        return currentVariable
    }

    override fun isVariable(value: String): Boolean {
        if (value.startsWith('"')) {
            if (value.length > 1) {
                if (value.endsWith('"')) {
                    return false
                }
            }
            throw InterpreterException(ExceptionType.INVALID_STRING)
        }

        return true
    }

    override suspend fun getBaseType(value: Any?, canBeStringField: Boolean): Any {
        if (value == null || value == Unit)
            throw InterpreterException(ExceptionType.INVALID_BLOCK)

        when (value) {
            is String -> {
                if (!canBeStringField) {
                    return value
                }

                value.toDoubleOrNull()?.let {
                    return it
                }

                value.toBooleanStrictOrNull()?.let {
                    return it
                }

                return if (isVariable(value)) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )

                    foundedStoredVariable.value!!
                } else {
                    value.drop(1).dropLast(1)
                }
            }

            is StoredVariable -> return value.value!! //default init
            is IOBlockBase -> return getBaseType(
                interpretBlocksUseCases.interpretIOBlockUseCase.interpretIOBlocks(
                    value
                ), true
            )
            is BlockBase -> return getBaseType(interpretBlock(value), false)
        }

        return value
    }

    override suspend fun interpretBlock(block: BlockBase) =
        when (block.type) {
            BlockType.CONDITION -> interpretBlocksUseCases.interpretConditionUseCase.interpretConditionBlocks(
                block as ConditionBlock
            )
            BlockType.EXPRESSION -> interpretBlocksUseCases.interpretExpressionBlockUseCase.interpretExpressionBlocks(
                block as ExpressionBlock
            )
            BlockType.IO -> interpretBlocksUseCases.interpretIOBlockUseCase.interpretIOBlocks(block as IOBlock)
            BlockType.LOOP -> interpretBlocksUseCases.interpretLoopBlockUseCase.interpretLoopBlocks(
                block as LoopBlock
            )
            BlockType.VARIABLE ->interpretBlocksUseCases.interpretVariableBlockUseCase.interpretVariableBlocks(
                block as VariableBlock
            )
            BlockType.START -> interpreterUseCases.interpretStartBlock.interpretStartBlock(block as StartBlock)
            BlockType.ARRAY -> interpretBlocksUseCases.interpretArrayBlockUseCase.interpretArrayBlock(
                block as ArrayBlock
            )
            BlockType.IF -> interpretBlocksUseCases.interpretIfBlockUseCase.interpretIfBlock(block as IfBlock)
        }
}


