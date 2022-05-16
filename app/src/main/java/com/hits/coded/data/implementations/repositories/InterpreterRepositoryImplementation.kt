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
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.domain.repositories.InterpreterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpreterRepositoryImplementation @Inject
constructor(
    private val consoleUseCases: ConsoleUseCases,
    private val heapUseCases: HeapUseCases
) : InterpreterRepository() {

    override var currentId = 0
        private set

    @Throws(InterpreterException::class)
    override suspend fun interpretStartBlock(startBlock: StartBlock) {
        startBlock.nestedBlocks?.forEach { nestedBlock ->
            interpretBlock(nestedBlock)
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase): Boolean {
        conditionBlock.id?.let {
            currentId = it
        }

        var conditionIsTrue = false

        val leftSide = getBaseType(conditionBlock.leftSide)

        var rightSide: Any? = null

        if (conditionBlock.logicalBlock?.logicalBlockType != LogicalBlockType.NOT) {
            rightSide = getBaseType(conditionBlock.rightSide)
        }

        conditionBlock.logicalBlock?.let {
            if (leftSide !is Boolean)
                throw InterpreterException(ExceptionType.TYPE_MISMATCH)

            when (it.logicalBlockType) {
                LogicalBlockType.AND -> {
                    if (rightSide !is Boolean)
                        throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                    conditionIsTrue = leftSide && rightSide
                }

                LogicalBlockType.OR -> {
                    if (rightSide !is Boolean)
                        throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                    conditionIsTrue = leftSide || rightSide
                }

                LogicalBlockType.NOT -> conditionIsTrue = !leftSide
            }
        }

        conditionBlock.mathematicalBlock?.let {
            val resultOfComparison = when (leftSide) {
                is String -> leftSide.compareTo(
                    convertAnyToString(conditionBlock.rightSide!!)
                )

                is Number -> leftSide.toDouble().compareTo(
                    convertAnyToDouble(conditionBlock.rightSide!!)
                )

                is Boolean -> leftSide.compareTo(
                    convertAnyToBoolean(conditionBlock.rightSide!!)
                )

                else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }

            conditionIsTrue = when (it.mathematicalBlockType) {
                MathematicalBlockType.EQUAL -> resultOfComparison == 0
                MathematicalBlockType.GREATER_OR_EQUAL_THAN -> resultOfComparison >= 0
                MathematicalBlockType.GREATER_THAN -> resultOfComparison > 0
                MathematicalBlockType.LOWER_OR_EQUAL_THAN -> resultOfComparison <= 0
                MathematicalBlockType.LOWER_THAN -> resultOfComparison < 0
                MathematicalBlockType.NON_EQUAL -> resultOfComparison != 0
            }

        }

        if (conditionIsTrue) {
            conditionBlock.nestedBlocks?.let {
                it.forEach { blockBase ->
                    when (blockBase.type) {
                        BlockType.VARIABLE -> interpretVariableBlocks(blockBase as VariableBlock)
                        BlockType.CONDITION -> interpretConditionBlocks(blockBase as ConditionBlock)
                        BlockType.LOOP -> interpretLoopBlocks(blockBase as LoopBlock)
                        BlockType.EXPRESSION -> interpretExpressionBlocks(blockBase as ExpressionBlock)
                        else -> {}
                    }
                }
            }
        }
        return conditionIsTrue
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretIfBlock(ifBlock: IfBlockBase) {
        ifBlock.id?.let {
            currentId = it
        }

        if (convertAnyToBoolean(ifBlock.conditionBlock)) {
            ifBlock.nestedBlocks?.let {
                it.forEach { blockBase ->
                    interpretBlock(blockBase)
                }
            }
        } else if (ifBlock.ifBlockType == IfBlockType.IF_WITH_ELSE) {
            ifBlock.elseBlocks?.let {
                it.forEach { blockBase ->
                    interpretBlock(blockBase)
                }
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretLoopBlocks(loopBlock: LoopBlockBase) {
        loopBlock.id?.let {
            currentId = it
        }

        loopBlock.nestedBlocks?.let {
            while (convertAnyToBoolean(loopBlock.conditionBlock)) {
                it.forEach { blockBase ->
                    interpretBlock(blockBase)
                }
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretVariableBlocks(variable: VariableBlockBase) {
        variable.id?.let {
            currentId = it
        }

        val variableParams: Any = variable.variableParams
            ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_SET -> {
                val valueToSet: Any = variable.valueToSet
                    ?: throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                val currentStoredVariable = getVariable(variableParams)
                currentStoredVariable.value = when (currentStoredVariable.type) {
                    VariableType.STRING -> convertAnyToString(valueToSet)
                    VariableType.INT -> convertAnyToInt(valueToSet)
                    VariableType.DOUBLE -> convertAnyToDouble(valueToSet)
                    VariableType.BOOLEAN -> convertAnyToBoolean(valueToSet)
                    VariableType.ARRAY -> convertAnyToArrayBase(
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
                    val currentStoredVariable = getVariable(variableParams)

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

    @Throws(InterpreterException::class)
    private suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase): Any {
        expressionBlock.id?.let {
            currentId = it
        }

        val leftSide = getBaseType(expressionBlock.leftSide)
        val rightSide = getBaseType(expressionBlock.rightSide)

        if (leftSide is Number && rightSide is Number) {

            val leftSideUnwrapped = leftSide.toDouble()
            val rightSideUnwrapped = rightSide.toDouble()

            return when (expressionBlock.expressionBlockType) {
                ExpressionBlockType.PLUS -> leftSideUnwrapped + rightSideUnwrapped

                ExpressionBlockType.MULTIPLY -> leftSideUnwrapped * rightSideUnwrapped

                ExpressionBlockType.DIVIDE -> {
                    if (rightSideUnwrapped == 0.0) {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }

                    leftSideUnwrapped / rightSideUnwrapped
                }

                ExpressionBlockType.MINUS -> leftSideUnwrapped - rightSideUnwrapped

                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> {
                    if (rightSideUnwrapped == 0.0) {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }

                    leftSideUnwrapped % rightSideUnwrapped
                }

                null -> throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
            }
        }

        if (leftSide is String &&
            rightSide is String &&
            expressionBlock.expressionBlockType == ExpressionBlockType.PLUS
        ) {
            return leftSide + rightSide
        }

        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretIOBlocks(IO: IOBlockBase): String? {
        IO.id?.let {
            currentId = it
        }

        return when (IO.ioBlockType) {
            IOBlockType.WRITE -> {
                IO.argument?.let {
                    consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(
                        convertAnyToStringIndulgently(it)
                    )
                }

                null
            }

            IOBlockType.READ -> {
                consoleUseCases.readFromConsoleUseCase.readFromConsole()
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretArrayBlock(block: ArrayBlockBase): Any {
        block.id?.let {
            currentId = it
        }

        val storedArray = when (val arrayIdentifier = block.array) {
            is String -> heapUseCases.getVariableUseCase.getVariable(arrayIdentifier)
            is ArrayBlockBase -> (interpretArrayBlock(arrayIdentifier) as? StoredVariable)
            else -> null
        }
            ?: throw InterpreterException(
                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
            )


        if (storedArray.isArray != true)
            throw InterpreterException(
                ExceptionType.TYPE_MISMATCH
            )

        val array = storedArray.value!! as ArrayBase // array in heap -> it has been constructed

        return when (block.arrayBlockType) {
            ArrayBlockType.GET_SIZE -> array.size
            ArrayBlockType.GET_ELEMENT -> array[convertAnyToInt(block.value)]
            ArrayBlockType.PUSH -> array.push(block.value)
            ArrayBlockType.POP -> array.pop()
            ArrayBlockType.CONCAT -> array.concat(block.value as? ArrayBase)
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretBlock(block: BlockBase) =
        when (block.type) {
            BlockType.CONDITION -> interpretConditionBlocks(block as ConditionBlock)
            BlockType.EXPRESSION -> interpretExpressionBlocks(block as ExpressionBlock)
            BlockType.IO -> interpretIOBlocks(block as IOBlock)
            BlockType.LOOP -> interpretLoopBlocks(block as LoopBlock)
            BlockType.VARIABLE -> interpretVariableBlocks(block as VariableBlock)
            BlockType.START -> interpretStartBlock(block as StartBlock)
            BlockType.ARRAY -> interpretArrayBlock(block as ArrayBlock)
            BlockType.IF -> interpretIfBlock(block as IfBlock)
        }


    @Throws(InterpreterException::class)
    private suspend fun convertAnyToDouble(value: Any?): Double {
        val processedValue = getBaseType(value)
        if (processedValue !is Number)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue.toDouble()
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToInt(value: Any?): Int {
        val processedValue = getBaseType(value)
        if (processedValue !is Number)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue.toInt()
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToBoolean(value: Any?): Boolean {
        val processedValue = getBaseType(value)
        if (processedValue !is Boolean)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue

    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToString(value: Any): String {
        val processedValue = getBaseType(value)
        if (processedValue !is String)
            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

        return processedValue
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToStringIndulgently(value: Any): String =
        when (val processedValue = getBaseType(value)) {
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

    private suspend fun convertAnyToArrayBase(value: Any, array: ArrayBase): ArrayBase {
        var canBeStringField = true
        val processedValue: Any
        when (value) {
            is BlockBase -> {
                processedValue = interpretBlock(value)
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
                    isVariable(processedValue) &&
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

    @Throws(InterpreterException::class)
    private suspend fun getBaseType(value: Any?, canBeStringField: Boolean = true): Any {
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
            is IOBlockBase -> return getBaseType(interpretIOBlocks(value), true)
            is BlockBase -> return getBaseType(interpretBlock(value), false)
        }

        return value
    }


    @Throws(InterpreterException::class)
    private fun isVariable(value: String): Boolean {
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
    private suspend fun getVariable(variableIdentifier: Any): StoredVariable {
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
                val value = interpretArrayBlock(variableIdentifier)
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
}