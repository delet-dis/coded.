package com.hits.coded.data.repositoriesImplementations

import android.util.Log
import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ArrayBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ConditionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalOperatorType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalOperatorType
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
    override suspend fun interpretStartBlock(start: StartBlock) {
        start.nestedBlocks?.forEach { nestedBlock ->
            when (nestedBlock.type) {
                BlockType.VARIABLE -> interpretVariableBlocks(nestedBlock as VariableBlockBase)
                BlockType.CONDITION -> interpretConditionBlocks(nestedBlock as ConditionBlockBase)
                BlockType.LOOP -> interpretLoopBlocks(nestedBlock as LoopBlockBase)
                BlockType.EXPRESSION -> interpretExpressionBlocks(nestedBlock as ExpressionBlockBase)
                BlockType.START -> throw InterpreterException(ExceptionType.WRONG_START_POSITION)
                BlockType.IO -> interpretIOBlocks(nestedBlock as IOBlock)
                BlockType.ARRAY -> interpretArrayBlock(nestedBlock as ArrayBlockBase)
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretConditionBlocks(condition: ConditionBlockBase): Boolean {
        condition.id?.let {
            currentId = it
        }
        var conditionIsTrue = false
        val leftSideType = getTypeOfAny(condition.leftSide)
        val rightSideType = getTypeOfAny(condition.rightSide)
        if (condition.logicalOperator != null) {
            when (condition.logicalOperator!!.logicalOperatorType) {

                LogicalOperatorType.AND -> {
                    when {
                        condition.rightSide != null && leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN -> {
                            conditionIsTrue =
                                (convertAnyToBoolean(condition.leftSide) && convertAnyToBoolean(
                                    condition.rightSide!!
                                ))
                        }
                        condition.rightSide != null -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                        else -> throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
                    }

                }
                LogicalOperatorType.NOT -> {
                    if (leftSideType == VariableType.BOOLEAN) conditionIsTrue =
                        !(convertAnyToBoolean(condition.leftSide))
                    else {
                        throw  InterpreterException(
                            ExceptionType.TYPE_MISMATCH
                        )

                    }
                }
                LogicalOperatorType.OR -> {
                    when {
                        condition.rightSide != null && leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN -> {
                            conditionIsTrue =
                                (convertAnyToBoolean(condition.leftSide) || convertAnyToBoolean(
                                    condition.rightSide!!
                                ))
                        }
                        condition.rightSide != null -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                        else -> throw  InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)

                    }

                }
            }
        } else {
            if (condition.rightSide != null) {
                condition.mathematicalOperator?.let {
                    when (it.mathematicalOperatorType) {
                        MathematicalOperatorType.EQUAL -> {
                            if (rightSideType == leftSideType) {
                                conditionIsTrue =
                                    (convertAnyToDouble(condition.leftSide) == convertAnyToDouble(
                                        condition.rightSide!!
                                    ))
                            } else {
                                throw  InterpreterException(ExceptionType.TYPE_MISMATCH)

                            }
                        }
                        MathematicalOperatorType.GREATER_OR_EQUAL_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.leftSide) >= convertAnyToDouble(
                                            condition.rightSide!!
                                        ))
                                }
                                rightSideType == leftSideType -> throw  InterpreterException(
                                    ExceptionType.WRONG_OPERAND_USE_CASE
                                )

                                else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                            }
                        }
                        MathematicalOperatorType.GREATER_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.leftSide) > convertAnyToDouble(
                                            condition.rightSide!!
                                        ))
                                }
                                rightSideType == leftSideType -> throw  InterpreterException(
                                    ExceptionType.WRONG_OPERAND_USE_CASE
                                )

                                else -> throw  InterpreterException(ExceptionType.TYPE_MISMATCH)

                            }
                        }
                        MathematicalOperatorType.LOWER_OR_EQUAL_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.leftSide) <= convertAnyToDouble(
                                            condition.rightSide!!
                                        ))
                                }
                                rightSideType == leftSideType -> throw  InterpreterException(
                                    ExceptionType.WRONG_OPERAND_USE_CASE
                                )

                                else -> throw  InterpreterException(
                                    ExceptionType.TYPE_MISMATCH
                                )

                            }
                        }
                        MathematicalOperatorType.LOWER_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.rightSide!!) > convertAnyToDouble(
                                            condition.leftSide
                                        ))
                                }
                                rightSideType == leftSideType -> throw  InterpreterException(
                                    ExceptionType.WRONG_OPERAND_USE_CASE
                                )

                                else -> throw  InterpreterException(
                                    ExceptionType.TYPE_MISMATCH
                                )

                            }
                        }
                        MathematicalOperatorType.NON_EQUAL -> {
                            if (rightSideType == leftSideType) {
                                conditionIsTrue =
                                    (convertAnyToDouble(condition.leftSide) != convertAnyToDouble(
                                        condition.rightSide!!
                                    ))
                            } else {
                                throw  InterpreterException(
                                    ExceptionType.TYPE_MISMATCH
                                )

                            }
                        }
                    }
                }
            } else {
                throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
            }
        }
        if (conditionIsTrue) {
            if (condition.nestedBlocks != null) {
                for (nestedBlock in condition.nestedBlocks!!) {
                    when (nestedBlock.type) {
                        BlockType.VARIABLE -> interpretVariableBlocks(nestedBlock as VariableBlock)
                        BlockType.CONDITION -> interpretConditionBlocks(nestedBlock as ConditionBlock)
                        BlockType.LOOP -> interpretLoopBlocks(nestedBlock as LoopBlock)
                        BlockType.EXPRESSION -> interpretExpressionBlocks(nestedBlock as ExpressionBlock)
                        else -> {}
                    }
                }
            }
        }
        return conditionIsTrue
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretLoopBlocks(loop: LoopBlockBase) {
        loop.id?.let {
            currentId = it
        }
        if (loop.nestedBlocks != null) {
            while (interpretConditionBlocks(loop.conditionBlock as ConditionBlock)) {
                for (nestedBlock in loop.nestedBlocks!!) {
                    when (nestedBlock.type) {
                        BlockType.VARIABLE -> interpretVariableBlocks(nestedBlock as VariableBlock)
                        BlockType.CONDITION -> interpretConditionBlocks(nestedBlock as ConditionBlock)
                        BlockType.LOOP -> interpretLoopBlocks(nestedBlock as LoopBlock)
                        BlockType.EXPRESSION -> interpretExpressionBlocks(nestedBlock as ExpressionBlock)
                        else -> {
                            throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                        }
                    }
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
                currentStoredVariable.value = when (currentStoredVariable.value) {
                    is String -> convertAnyToString(valueToSet)
                    is Int -> convertAnyToInt(valueToSet)
                    is Double -> convertAnyToDouble(valueToSet)
                    is Boolean -> convertAnyToBoolean(valueToSet)
                    is ArrayBase -> convertAnyToArrayBase(
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
    private suspend fun interpretExpressionBlocks(expression: ExpressionBlockBase): Any {
        expression.id?.let {
            currentId = it
        }
        val leftSideAsString = expression.leftSide
        val rightSideAsString = expression.rightSide
        val leftSideType: VariableType? = getTypeOfAny(leftSideAsString)
        val rightSideType: VariableType? = getTypeOfAny(rightSideAsString)
        Log.d(leftSideType.toString(), rightSideType.toString())
        if (leftSideType == VariableType.DOUBLE && VariableType.DOUBLE == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> {
                    return (convertAnyToDouble(
                        leftSideAsString!!
                    ) + convertAnyToDouble(
                        rightSideAsString!!
                    ))
                }
                ExpressionBlockType.MULTIPLY -> return (convertAnyToDouble(leftSideAsString!!) * convertAnyToDouble(
                    rightSideAsString!!
                ))
                ExpressionBlockType.DIVIDE -> {
                    if (convertAnyToDouble(
                            rightSideAsString!!
                        ) != 0.0
                    ) {
                        return (convertAnyToDouble(leftSideAsString!!) / convertAnyToDouble(
                            rightSideAsString
                        ))
                    } else {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }
                }
                ExpressionBlockType.MINUS -> return (convertAnyToDouble(leftSideAsString!!) - convertAnyToDouble(
                    rightSideAsString!!
                ))
                ExpressionBlockType.DIVIDE_WITH_REMAINDER ->
                    throw InterpreterException(ExceptionType.WRONG_OPERAND_USE_CASE)
            }
        }
        if (leftSideType == VariableType.INT && VariableType.INT == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> return (convertAnyToInt(leftSideAsString!!) + convertAnyToInt(
                    rightSideAsString!!
                ))
                ExpressionBlockType.MULTIPLY -> return (convertAnyToInt(leftSideAsString!!) * convertAnyToInt(
                    rightSideAsString!!
                ))
                ExpressionBlockType.DIVIDE -> {
                    if (convertAnyToInt(
                            rightSideAsString!!
                        ) != 0
                    ) {
                        return (convertAnyToInt(leftSideAsString!!) / convertAnyToInt(
                            rightSideAsString
                        ))
                    } else {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }
                }
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> {
                    if (convertAnyToInt(
                            rightSideAsString!!
                        ) != 0
                    ) {
                        return (convertAnyToInt(leftSideAsString!!) % convertAnyToInt(
                            rightSideAsString
                        ))
                    } else {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }
                }
                ExpressionBlockType.MINUS -> return (convertAnyToInt(leftSideAsString!!) - convertAnyToInt(
                    rightSideAsString!!
                ))
            }
        }
        if (leftSideType == VariableType.STRING && VariableType.STRING == rightSideType && expression.expressionBlockType == ExpressionBlockType.PLUS) {
            return '"' + convertAnyToString(leftSideAsString!!) + convertAnyToString(
                rightSideAsString!!
            ) + '"'
        }
        if (leftSideType == null || rightSideType == null) {
            throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
        } else {
            throw  InterpreterException(ExceptionType.TYPE_MISMATCH)
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretIOBlocks(IO: IOBlockBase): String? {
        IO.id?.let {
            currentId = it
        }

        when (IO.ioBlockType) {
            IOBlockType.WRITE -> {
                IO.argument?.let {
                    consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(
                        convertAnyToStringIndulgently(it)
                    )
                }

                return null
            }

            IOBlockType.READ -> {
                val input = consoleUseCases.readFromConsoleUseCase.readFromConsole()
                (IO as IOBlock).argument = input
                return input
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretArrayBlock(block: ArrayBlockBase): Any {
        block.id?.let {
            currentId = it
        }

        val arrayIdentifier = block.array
        val storedArray = when (arrayIdentifier) {
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
    private suspend fun interpretBlock(block: BlockBase): Any? {
        return when (block.type) {
            BlockType.CONDITION -> interpretConditionBlocks(block as ConditionBlock)
            BlockType.EXPRESSION -> interpretExpressionBlocks(block as ExpressionBlock)
            BlockType.IO -> interpretIOBlocks(block as IOBlock)
            BlockType.LOOP -> interpretLoopBlocks(block as LoopBlock)
            BlockType.VARIABLE -> interpretVariableBlocks(block as VariableBlock)
            BlockType.START -> interpretStartBlock(block as StartBlock)
            BlockType.ARRAY -> interpretArrayBlock(block as ArrayBlockBase)
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToDouble(value: Any?): Double {
        when (value) {
            is Double -> return value
            is StoredVariable -> {
                if (value.value is Double) {
                    return value.value as Double
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }
            is ExpressionBlock -> {
                return interpretExpressionBlocks(value) as? Double
                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }
            is String -> {
                value.toDoubleOrNull()?.let {
                    return it
                }

                if (isVariable(value)) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    return convertAnyToDouble(foundedStoredVariable)
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }

            is ArrayBlock -> return convertAnyToDouble(interpretArrayBlock(value))

            is IOBlockBase -> {
                return interpretIOBlocks(value)?.toDoubleOrNull()
                    ?: throw InterpreterException(
                        ExceptionType.TYPE_MISMATCH
                    )
            }
        }

        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToInt(value: Any?): Int {
        when (value) {
            is Double -> throw InterpreterException(
                ExceptionType.TYPE_MISMATCH
            )
            is Int -> return value
            is StoredVariable -> {
                if (value.value is Int) {
                    return value.value as Int
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }
            is ExpressionBlock -> {
                return interpretExpressionBlocks(value) as? Int
                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }
            is IOBlockBase -> {
                return interpretIOBlocks(value)?.toIntOrNull()
                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }

            is String -> {
                value.toIntOrNull()?.let {
                    return it
                }
                if (isVariable(value)) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    return convertAnyToInt(foundedStoredVariable)
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }

            is ArrayBlock -> return convertAnyToInt(interpretArrayBlock(value))
        }
        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToBoolean(value: Any): Boolean {
        when (value) {
            is Boolean -> return value
            is StoredVariable -> {
                if (value.value is Boolean) {
                    return value.value as Boolean
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }
            is ExpressionBlock -> {
                return interpretExpressionBlocks(value) as? Boolean
                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }
            is String -> {
                value.toBooleanStrictOrNull()?.let {
                    return it
                }

                if (isVariable(value)) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    return convertAnyToBoolean(foundedStoredVariable)
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }
            is ArrayBlock -> return convertAnyToBoolean(interpretArrayBlock(value))
        }
        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToString(value: Any): String {
        when (value) {
            is StoredVariable -> {
                if (value.value is String) {
                    return value.value as String
                } else {
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                }
            }

            is ExpressionBlock -> {
                return interpretExpressionBlocks(value) as? String
                    ?: throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }
            is String -> {
                if (isVariable(value)) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    return convertAnyToString(foundedStoredVariable)
                } else {
                    return value.drop(1).dropLast(1)
                }
            }

            is ArrayBlock -> return convertAnyToString(interpretArrayBlock(value))

            is IOBlockBase -> {
                return interpretIOBlocks(value)
                    ?: throw InterpreterException(
                        ExceptionType.TYPE_MISMATCH
                    )
            }
        }
        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToStringIndulgently(value: Any, canBeStringField: Boolean = true): String {
        var processedValue = value
        if (processedValue is BlockBase) {
            processedValue = interpretBlock(processedValue)
                ?: throw InterpreterException(
                    ExceptionType.INVALID_BLOCK
                )
        }

        when (processedValue) {
            is StoredVariable -> return convertAnyToStringIndulgently(processedValue.value!!, false)

            is ArrayBase -> {
                var resultString = "[ "
                for (i in 0 until processedValue.size)
                    resultString += convertAnyToStringIndulgently(processedValue[i], false) + ' '
                resultString += " ]"

                return resultString
            }

            is String -> {
                if(!canBeStringField)
                    return processedValue

                if (isVariable(processedValue)) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(processedValue)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    return convertAnyToStringIndulgently(foundedStoredVariable, false)
                } else {
                    return processedValue.drop(1).dropLast(1)
                }
            }

            else -> return processedValue.toString()
        }
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
                if (canBeStringField &&
                    isVariable(processedValue) &&
                    heapUseCases.isVariableDeclaredUseCase.isVariableDeclared(processedValue)
                ) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(processedValue)
                            ?: throw InterpreterException(
                                ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                            )
                    return convertAnyToArrayBase(foundedStoredVariable, array)
                } else {
                    return array.parseArray(processedValue)
                }
            }

            else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
        }

    }

    @Throws(InterpreterException::class)
    private suspend fun getTypeOfAny(value: Any?): VariableType? {
        when (value) {
            is String -> {
                when {
                    value.toIntOrNull() is Int -> return VariableType.INT
                    value.toDoubleOrNull() is Double -> return VariableType.DOUBLE
                    value.toBooleanStrictOrNull() is Boolean -> return VariableType.BOOLEAN
                    else -> {
                        if (isVariable(value)) {
                            val foundedStoredVariable =
                                heapUseCases.getVariableUseCase.getVariable(value)
                            if (foundedStoredVariable == null) {
                                throw InterpreterException(
                                    ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                                )
                            } else {
                                return foundedStoredVariable.type

                            }
                        } else return VariableType.STRING
                    }

                }
            }
            is IOBlockBase -> {
                if (value.argument != null) {
                    return getTypeOfAny(value.argument!!)
                } else {
                    return interpretIOBlocks(value)?.let { getTypeOfAny(it) }!!
                }
            }
            is Double -> return VariableType.DOUBLE
            is Int -> return VariableType.INT
            is Boolean -> return VariableType.BOOLEAN
            is ExpressionBlockBase -> return getTypeOfAny(interpretExpressionBlocks(value))
        }
        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
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