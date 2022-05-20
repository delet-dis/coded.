package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ArrayBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IfBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.ConditionBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterBlocksUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class InterpreterAuxiliaryRepositoryImplementation
@Inject constructor(
    private val heapUseCases: HeapUseCases,
    private val interpreterBlocksUseCases: Provider<InterpreterBlocksUseCases>
) : InterpreterAuxiliaryRepository() {
    @Throws(InterpreterException::class)
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
                val value =
                    interpreterBlocksUseCases.get().interpretArrayBlockUseCase.interpretArrayBlock(
                        variableIdentifier
                    )

                if (value !is StoredVariable) {
                    throw InterpreterException(ExceptionType.INVALID_BLOCK)
                }

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

    @Throws(InterpreterException::class)
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

    @Throws(InterpreterException::class)
    override suspend fun getBaseType(
        value: Any?,
        canBeVariableName: Boolean,
        castToNumber: Boolean
    ): Any {
        if (value == null || value == Unit)
            throw InterpreterException(ExceptionType.INVALID_BLOCK)

        when (value) {
            is String -> {
                if (castToNumber) {
                    value.toDoubleOrNull()?.let {
                        return it
                    }

                    value.toBooleanStrictOrNull()?.let {
                        return it
                    }
                }

                if (canBeVariableName) {
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

                return value
            }

            is StoredVariable -> return value.value!! //default init
            is IOBlockBase -> return getBaseType(
                interpreterBlocksUseCases.get().interpretIOBlockUseCase.interpretIOBlocks(
                    value
                ), false,
                castToNumber
            )
            is BlockBase -> return getBaseType(interpretBlock(value), false, castToNumber)
        }

        return value
    }

    @Throws(InterpreterException::class)
    override suspend fun interpretBlock(block: BlockBase) =
        when (block.type) {
            BlockType.CONDITION -> interpreterBlocksUseCases.get().interpretConditionUseCase.interpretConditionBlocks(
                block as ConditionBlock
            )
            BlockType.EXPRESSION -> interpreterBlocksUseCases.get().interpretExpressionBlockUseCase.interpretExpressionBlocks(
                block as ExpressionBlock
            )
            BlockType.IO -> interpreterBlocksUseCases.get().interpretIOBlockUseCase.interpretIOBlocks(
                block as IOBlock
            )
            BlockType.LOOP -> interpreterBlocksUseCases.get().interpretLoopBlockUseCase.interpretLoopBlocks(
                block as LoopBlock
            )
            BlockType.VARIABLE -> interpreterBlocksUseCases.get().interpretVariableBlockUseCase.interpretVariableBlocks(
                block as VariableBlock
            )
            BlockType.ARRAY -> interpreterBlocksUseCases.get().interpretArrayBlockUseCase.interpretArrayBlock(
                block as ArrayBlock
            )
            BlockType.IF -> interpreterBlocksUseCases.get().interpretIfBlockUseCase.interpretIfBlock(
                block as IfBlock
            )
            else -> {
                throw InterpreterException(ExceptionType.INVALID_BLOCK)
            }
        }
}


