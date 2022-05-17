package com.hits.coded.data.implementations.repositories

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ArrayBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IfBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.ConditionBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalBlockType
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreter.useCases.InterpretBlocksUseCases
import com.hits.coded.data.models.interpreter.useCases.InterpreterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.domain.repositories.InterpreterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpreterRepositoryImplementation @Inject
constructor(
    private val interpretBlocksUseCases: InterpretBlocksUseCases,
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases
) : InterpreterRepository() {

    override var currentId = 0
        private set

    @Throws(InterpreterException::class)
    override suspend fun interpretStartBlock(startBlock: StartBlock) {
        startBlock.nestedBlocks?.forEach { nestedBlock ->
            auxiliaryFunctionsUseCases.interpretBlocksUseCase.interpretBlock(nestedBlock)
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase): Boolean {
        return interpretBlocksUseCases.interpretConditionUseCase.interpretConditionBlocks(conditionBlock)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretIfBlock(ifBlock: IfBlockBase) {
        interpretBlocksUseCases.interpretIfBlockUseCase.interpretIfBlock(ifBlock)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretLoopBlocks(loopBlock: LoopBlockBase) {
        interpretBlocksUseCases.interpretLoopBlockUseCase.interpretLoopBlocks(loopBlock)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretVariableBlocks(variable: VariableBlockBase) {
        interpretBlocksUseCases.interpretVariableBlockUseCase.interpretVariableBlocks(variable)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase): Any {
        return interpretBlocksUseCases.interpretExpressionBlockUseCase.interpretExpressionBlocks(expressionBlock)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretIOBlocks(IO: IOBlockBase): String? {
        return interpretBlocksUseCases.interpretIOBlockUseCase.interpretIOBlocks(IO)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretArrayBlock(block: ArrayBlockBase): Any {
        return interpretBlocksUseCases.interpretArrayBlockUseCase.interpretArrayBlock(block)
    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun interpretBlock(block: BlockBase) =
//        when (block.type) {
//            BlockType.CONDITION -> interpretConditionBlocks(block as ConditionBlock)
//            BlockType.EXPRESSION -> interpretExpressionBlocks(block as ExpressionBlock)
//            BlockType.IO -> interpretIOBlocks(block as IOBlock)
//            BlockType.LOOP -> interpretLoopBlocks(block as LoopBlock)
//            BlockType.VARIABLE -> interpretVariableBlocks(block as VariableBlock)
//            BlockType.START -> interpretStartBlock(block as StartBlock)
//            BlockType.ARRAY -> interpretArrayBlock(block as ArrayBlock)
//            BlockType.IF -> interpretIfBlock(block as IfBlock)
//        }
//
//
//    @Throws(InterpreterException::class)
//    private suspend fun convertAnyToDouble(value: Any?): Double {
//        val processedValue = getBaseType(value)
//        if (processedValue !is Number)
//            throw InterpreterException(ExceptionType.TYPE_MISMATCH)
//
//        return processedValue.toDouble()
//    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun convertAnyToInt(value: Any?): Int {
//        val processedValue = getBaseType(value)
//        if (processedValue !is Number)
//            throw InterpreterException(ExceptionType.TYPE_MISMATCH)
//
//        return processedValue.toInt()
//    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun convertAnyToBoolean(value: Any?): Boolean {
//        val processedValue = getBaseType(value)
//        if (processedValue !is Boolean)
//            throw InterpreterException(ExceptionType.TYPE_MISMATCH)
//
//        return processedValue
//
//    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun convertAnyToString(value: Any): String {
//        val processedValue = getBaseType(value)
//        if (processedValue !is String)
//            throw InterpreterException(ExceptionType.TYPE_MISMATCH)
//
//        return processedValue
//    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun convertAnyToStringIndulgently(value: Any): String =
//        when (val processedValue = getBaseType(value)) {
//            is ArrayBase -> {
//                var resultString = "[ "
//                for (i in 0 until processedValue.size)
//                    resultString += convertAnyToStringIndulgently(processedValue[i]) + ' '
//                resultString += " ]"
//
//                resultString
//            }
//
//            is String -> processedValue
//            else -> processedValue.toString()
//        }
//
//    private suspend fun convertAnyToArrayBase(value: Any, array: ArrayBase): ArrayBase {
//        var canBeStringField = true
//        val processedValue: Any
//        when (value) {
//            is BlockBase -> {
//                processedValue = interpretBlock(value)
//                    ?: throw InterpreterException(
//                        ExceptionType.INVALID_BLOCK
//                    )
//                canBeStringField = false
//            }
//            else -> processedValue = value
//        }
//
//        when (processedValue) {
//            is StoredVariable -> {
//                return processedValue.value as? ArrayBase
//                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
//            }
//
//            is String -> {
//                return if (canBeStringField &&
//                    isVariable(processedValue) &&
//                    heapUseCases.isVariableDeclaredUseCase.isVariableDeclared(processedValue)
//                ) {
//                    val foundedStoredVariable =
//                        heapUseCases.getVariableUseCase.getVariable(processedValue)
//                            ?: throw InterpreterException(
//                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
//                            )
//                    convertAnyToArrayBase(foundedStoredVariable, array)
//                } else {
//                    array.parseArray(processedValue)
//                }
//            }
//
//            else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
//        }
//    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun getBaseType(value: Any?, canBeStringField: Boolean = true): Any {
//        if (value == null || value == Unit)
//            throw InterpreterException(ExceptionType.INVALID_BLOCK)
//
//        when (value) {
//            is String -> {
//                if (!canBeStringField) {
//                    return value
//                }
//
//                value.toDoubleOrNull()?.let {
//                    return it
//                }
//
//                value.toBooleanStrictOrNull()?.let {
//                    return it
//                }
//
//                return if (isVariable(value)) {
//                    val foundedStoredVariable =
//                        heapUseCases.getVariableUseCase.getVariable(value)
//                            ?: throw InterpreterException(
//                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
//                            )
//
//                    foundedStoredVariable.value!!
//                } else {
//                    value.drop(1).dropLast(1)
//                }
//            }
//
//            is StoredVariable -> return value.value!! //default init
//            is IOBlockBase -> return getBaseType(interpretIOBlocks(value), true)
//            is BlockBase -> return getBaseType(interpretBlock(value), false)
//        }
//
//        return value
//    }
//
//
//    @Throws(InterpreterException::class)
//    private fun isVariable(value: String): Boolean {
//        if (value.startsWith('"')) {
//            if (value.length > 1) {
//                if (value.endsWith('"')) {
//                    return false
//                }
//            }
//            throw InterpreterException(ExceptionType.INVALID_STRING)
//        }
//
//        return true
//    }
//
//    @Throws(InterpreterException::class)
//    private suspend fun getVariable(variableIdentifier: Any): StoredVariable {
//        val currentVariable: StoredVariable
//        when (variableIdentifier) {
//            is String -> {
//                currentVariable =
//                    heapUseCases.getVariableUseCase.getVariable(variableIdentifier)
//                        ?: throw InterpreterException(
//                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
//                        )
//            }
//            is ArrayBlockBase -> {
//                val value = interpretArrayBlock(variableIdentifier)
//                if (value !is StoredVariable)
//                    throw InterpreterException(ExceptionType.INVALID_BLOCK)
//
//                currentVariable = value
//            }
//
//            is StoredVariable -> {
//                if (variableIdentifier.name == null)
//                    throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
//
//                currentVariable =
//                    heapUseCases.getVariableUseCase.getVariable(variableIdentifier.name!!)
//                        ?: throw InterpreterException(
//                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
//                        )
//            }
//
//            else -> throw InterpreterException(ExceptionType.INVALID_BLOCK)
//        }
//
//        return currentVariable
//    }
}