package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.ConditionBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalBlockType
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
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

    private var currentId = 0
    private var ioBlocksValues: HashMap<Int, Any> = HashMap()

    @Throws(InterpreterException::class)
    override suspend fun interpretStartBlock(startBlock: StartBlock) {
        ioBlocksValues.clear()

        startBlock.nestedBlocks?.forEach { nestedBlock ->
            when (nestedBlock.type) {
                BlockType.VARIABLE -> interpretVariableBlocks(nestedBlock as VariableBlockBase)
                BlockType.CONDITION -> interpretConditionBlocks(nestedBlock as ConditionBlockBase)
                BlockType.LOOP -> interpretLoopBlocks(nestedBlock as LoopBlockBase)
                BlockType.EXPRESSION -> interpretExpressionBlocks(nestedBlock as ExpressionBlockBase)
                BlockType.START -> throw InterpreterException(
                    currentId,
                    ExceptionType.WRONG_START_POSITION
                )
                BlockType.IO -> interpretIOBlocks(nestedBlock as IOBlock)
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretConditionBlocks(conditionBlock: ConditionBlockBase): Boolean {
        conditionBlock.id?.let {
            currentId = it
        }

        var conditionIsTrue = false

        val leftSideType = getTypeOfAny(conditionBlock.leftSide)

        var rightSideType: VariableType? = null

        if (conditionBlock.logicalBlock?.logicalBlockType != LogicalBlockType.NOT) {
            rightSideType = getTypeOfAny(conditionBlock.rightSide)
        }

        conditionBlock.logicalBlock?.let {
            when (it.logicalBlockType) {
                LogicalBlockType.AND,
                LogicalBlockType.OR -> {
                    when {
                        leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN -> {
                            val leftSide = convertAnyToBoolean(conditionBlock.leftSide!!)

                            val rightSide = convertAnyToBoolean(conditionBlock.rightSide!!)

                            if (it.logicalBlockType == LogicalBlockType.AND) {
                                conditionIsTrue = leftSide && rightSide
                            }

                            if (it.logicalBlockType == LogicalBlockType.OR) {
                                conditionIsTrue = leftSide || rightSide
                            }
                        }

                        conditionBlock.rightSide != null && conditionBlock.leftSide != null ->
                            throw InterpreterException(
                                currentId,
                                ExceptionType.TYPE_MISMATCH
                            )

                        else -> throw InterpreterException(
                            currentId,
                            ExceptionType.LACK_OF_ARGUMENTS
                        )
                    }
                }

                LogicalBlockType.NOT -> {
                    if (leftSideType == VariableType.BOOLEAN) {
                        conditionIsTrue = !(convertAnyToBoolean(conditionBlock.leftSide!!))
                    } else {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.TYPE_MISMATCH
                        )
                    }
                }
            }
        }

        conditionBlock.mathematicalBlock?.let {
            if (leftSideType == rightSideType) {
                val resultOfComparison = when (leftSideType) {
                    VariableType.STRING -> convertAnyToString(conditionBlock.leftSide!!).compareTo(
                        convertAnyToString(conditionBlock.rightSide!!)
                    )

                    VariableType.DOUBLE -> convertAnyToDouble(conditionBlock.leftSide!!).compareTo(
                        convertAnyToDouble(conditionBlock.rightSide!!)
                    )

                    VariableType.INT -> convertAnyToInt(conditionBlock.leftSide!!).compareTo(
                        convertAnyToInt(conditionBlock.rightSide!!)
                    )

                    VariableType.BOOLEAN -> convertAnyToBoolean(conditionBlock.leftSide!!).compareTo(
                        convertAnyToBoolean(conditionBlock.rightSide!!)
                    )

                    else -> {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.TYPE_MISMATCH
                        )
                    }
                }

                conditionIsTrue = when (it.mathematicalBlockType) {
                    MathematicalBlockType.EQUAL -> resultOfComparison == 0
                    MathematicalBlockType.GREATER_OR_EQUAL_THAN -> resultOfComparison >= 0
                    MathematicalBlockType.GREATER_THAN -> resultOfComparison > 0
                    MathematicalBlockType.LOWER_OR_EQUAL_THAN -> resultOfComparison <= 0
                    MathematicalBlockType.LOWER_THAN -> resultOfComparison < 0
                    MathematicalBlockType.NON_EQUAL -> resultOfComparison != 0
                }
            } else {
                throw InterpreterException(
                    currentId,
                    ExceptionType.TYPE_MISMATCH
                )
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
    private suspend fun interpretLoopBlocks(loopBlock: LoopBlockBase) {
        loopBlock.id?.let {
            currentId = it
        }

        loopBlock.nestedBlocks?.let {
            while (interpretConditionBlocks(loopBlock.conditionBlock)) {
                it.forEach { blockBase ->
                    when (blockBase.type) {
                        BlockType.VARIABLE -> interpretVariableBlocks(blockBase as VariableBlockBase)
                        BlockType.CONDITION -> interpretConditionBlocks(blockBase as ConditionBlockBase)
                        BlockType.LOOP -> interpretLoopBlocks(blockBase as LoopBlockBase)
                        BlockType.EXPRESSION -> interpretExpressionBlocks(blockBase as ExpressionBlockBase)
                        else -> {
                            throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
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
        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_SET -> {
                when (variable.valueToSet) {
                    is ExpressionBlockBase -> {
                        val currentStoredVariable = variable.variableParams?.name?.let {
                            heapUseCases.getVariableUseCase.getVariable(it)
                        }
                        val expressionValueType = getTypeOfAny(variable.valueToSet)
                        if (expressionValueType == currentStoredVariable?.type) {
                            variable.variableParams?.name?.let {
                                when (expressionValueType) {
                                    VariableType.INT -> {
                                        heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                            it,
                                            convertAnyToInt(variable.valueToSet as ExpressionBlock)
                                        )
                                    }
                                    VariableType.DOUBLE -> {
                                        heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                            it,
                                            convertAnyToDouble(variable.valueToSet as ExpressionBlock)
                                        )
                                    }
                                    VariableType.STRING -> {
                                        heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                            it,
                                            convertAnyToString(variable.valueToSet as ExpressionBlock).drop(
                                                1
                                            ).dropLast(1)
                                        )
                                    }
                                    else -> {
                                        throw variable.id?.let { it1 ->
                                            InterpreterException(
                                                it1, ExceptionType.TYPE_MISMATCH
                                            )
                                        }!!
                                    }
                                }
                            }
                        } else throw variable.id?.let {
                            InterpreterException(
                                it,
                                ExceptionType.TYPE_MISMATCH
                            )
                        }!!
                    }
                    is String -> {
                        val currentStoredVariable = variable.variableParams?.name?.let {
                            heapUseCases.getVariableUseCase.getVariable(it)
                        }
                        if ((variable.valueToSet as String).toIntOrNull() is Int && currentStoredVariable?.type == VariableType.INT) {
                            currentStoredVariable?.name?.let {
                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                    it, (variable.valueToSet as String).toInt()
                                )
                            }
                        } else if ((variable.valueToSet as String).toDoubleOrNull() is Double && currentStoredVariable?.type == VariableType.DOUBLE) {
                            variable.variableParams?.name?.let {
                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                    it, (variable.valueToSet as String).toDouble()
                                )
                            }
                        } else if ((variable.valueToSet as String).toBooleanStrictOrNull() is Boolean && currentStoredVariable?.type == VariableType.BOOLEAN) {
                            variable.variableParams?.name?.let {
                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                    it, (variable.valueToSet as String).toBoolean()
                                )
                            }
                        } else if (!((variable.valueToSet as String)[0] == '"' && (variable.valueToSet as String)[(variable.valueToSet as String).lastIndex] == '"')) {
                            val foundedStoredVariable =
                                heapUseCases.getVariableUseCase.getVariable(variable.valueToSet as String)
                            if (foundedStoredVariable == null) {
                                throw variable.id?.let {
                                    InterpreterException(
                                        it,
                                        ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                                    )
                                }!!
                            } else {
                                if (foundedStoredVariable.type == currentStoredVariable?.type) {
                                    foundedStoredVariable.value?.let {
                                        variable.variableParams?.name?.let { it1 ->
                                            heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                it1,
                                                it
                                            )
                                        }
                                    }
                                } else {
                                    throw variable.id?.let {
                                        InterpreterException(
                                            it,
                                            ExceptionType.TYPE_MISMATCH
                                        )
                                    }!!
                                }
                            }
                        } else {
                            if (currentStoredVariable?.type == VariableType.STRING) {
                                variable.variableParams?.name?.let {
                                    heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                        it,
                                        (variable.valueToSet as String).drop(1).dropLast(1)
                                    )
                                }
                            }


                        }
                    }
                    is IOBlockBase -> {
                        val consoleValue = interpretIOBlocks(variable.valueToSet as IOBlockBase)
                        if (consoleValue != null) {
                            val currentStoredVariable = variable.variableParams?.name?.let {
                                heapUseCases.getVariableUseCase.getVariable(it)
                            }
                            if (currentStoredVariable?.type == VariableType.STRING) {
                                variable.variableParams?.name?.let {
                                    heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                        it,
                                        (consoleValue)
                                    )
                                }
                            } else {
                                val consoleValueType = getTypeOfAny(consoleValue)
                                if (consoleValueType == currentStoredVariable?.type) {
                                    variable.variableParams?.name?.let {
                                        heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                            it,
                                            (consoleValue)
                                        )
                                    }
                                } else {
                                    throw  InterpreterException(
                                        currentId,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }
                            }
                        } else {
                            throw  InterpreterException(currentId, ExceptionType.LACK_OF_ARGUMENTS)
                        }
                    }
                    is ConditionBlockBase -> {
                        val currentStoredVariable = variable.variableParams?.name?.let {
                            heapUseCases.getVariableUseCase.getVariable(it)
                        }
                        if (currentStoredVariable?.type == VariableType.BOOLEAN) {
                            variable.variableParams?.name?.let {
                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                    it,
                                    (convertAnyToBoolean(variable.valueToSet as ConditionBlockBase))
                                )
                            }
                        } else {
                            throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                        }
                    }
                }
            }
            VariableBlockType.VARIABLE_CHANGE -> {
                if (variable.valueToSet is String) {
                    val operand = (variable.valueToSet as String)[0]
                    val typeOfNewValue = getTypeOfAny((variable.valueToSet as String).drop(1))
                    val currentStoredVariable = variable.variableParams?.name?.let {
                        heapUseCases.getVariableUseCase.getVariable(it)
                    }
                    if (typeOfNewValue == currentStoredVariable?.type) {
                        when (typeOfNewValue) {
                            VariableType.INT -> {
                                val toAdd: Int = (variable.valueToSet as String).drop(1).toInt()
                                if (currentStoredVariable?.value != null) {
                                    when (operand) {
                                        '+' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    toAdd + convertAnyToInt(currentStoredVariable.value!!)
                                                )
                                            }

                                        }
                                        '-' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToInt(currentStoredVariable.value!!) - toAdd
                                                )
                                            }
                                        }
                                        '*' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToInt(currentStoredVariable.value!!) * toAdd
                                                )
                                            }
                                        }
                                        '/' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToInt(currentStoredVariable.value!!) / toAdd
                                                )
                                            }
                                        }
                                        '%' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToInt(currentStoredVariable.value!!) % toAdd
                                                )
                                            }
                                        }
                                        else -> {
                                            throw InterpreterException(
                                                currentId,
                                                ExceptionType.WRONG_OPERAND_USE_CASE
                                            )
                                        }
                                    }
                                } else {
                                    throw InterpreterException(
                                        currentId,
                                        ExceptionType.LACK_OF_ARGUMENTS
                                    )
                                }
                            }
                            VariableType.DOUBLE -> {
                                val toAdd: Double =
                                    (variable.valueToSet as String).drop(1).toDouble()
                                if (currentStoredVariable?.value != null) {
                                    when (operand) {
                                        '+' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    toAdd + convertAnyToDouble(currentStoredVariable.value!!)
                                                )
                                            }

                                        }
                                        '-' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToDouble(currentStoredVariable.value!!) - toAdd
                                                )
                                            }
                                        }
                                        '*' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToDouble(currentStoredVariable.value!!) * toAdd
                                                )
                                            }
                                        }
                                        '/' -> {
                                            currentStoredVariable.name?.let {
                                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                                    it,
                                                    convertAnyToDouble(currentStoredVariable.value!!) / toAdd
                                                )
                                            }
                                        }
                                        else -> {
                                            throw InterpreterException(
                                                currentId,
                                                ExceptionType.WRONG_OPERAND_USE_CASE
                                            )
                                        }
                                    }
                                } else {
                                    throw InterpreterException(
                                        currentId,
                                        ExceptionType.LACK_OF_ARGUMENTS
                                    )
                                }
                            }
                            else -> {
                                throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                            }
                        }
                    } else {
                        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                    }
                } else {
                    throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                }
                /*
                val typeOfNewValue = getTypeOfAny(variable.valueToSet)
                val currentStoredVariable = variable.variableParams?.name?.let {
                    heapUseCases.getVariableUseCase.getVariable(it)
                }
                if (typeOfNewValue == currentStoredVariable?.type) {
                    Log.d("finder1", "helpVariable1")
                    when (typeOfNewValue) {
                        VariableType.INT -> {
                            Log.d("chek1", "chek")
                            val toAdd: Int? = variable.valueToSet?.let {
                                convertAnyToInt(it)
                            }
                            Log.d("values", toAdd.toString())
                            Log.d("values2", currentStoredVariable?.value.toString())

                            if (toAdd != null && currentStoredVariable?.value != null) {
                                currentStoredVariable?.name?.let {
                                    heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                        it, toAdd + convertAnyToInt(currentStoredVariable.value!!)
                                    )
                                }
                            }
                        }
                        VariableType.DOUBLE -> {
                            Log.d("finder3", "helpVariable3")
                            val toAdd: Double =
                                variable.valueToSet?.let { convertAnyToDouble(it) }!!
                            currentStoredVariable?.name?.let {
                                heapUseCases.reAssignVariableUseCase.reAssignVariable(
                                    it,
                                    (convertAnyToDouble(currentStoredVariable.value!!)) + toAdd
                                )
                            }
                        }
                        else -> {
                            Log.d("finder4", "helpVariable4")
                            throw  variable.id?.let {
                                InterpreterException(
                                    it,
                                    ExceptionType.TYPE_MISMATCH
                                )
                            }!!
                        }
                    }
                } else {
                    throw variable.id?.let {
                        InterpreterException(
                            it,
                            ExceptionType.TYPE_MISMATCH
                        )
                    }!!
                }*/
            }
            VariableBlockType.VARIABLE_CREATE -> {
                variable.variableParams?.let {
                    heapUseCases.addVariableUseCase.addVariable(it)
                }
            }

            else -> {
                throw variable.id?.let {
                    InterpreterException(
                        it,
                        ExceptionType.WRONG_OPERAND_USE_CASE
                    )
                }!!
            }
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretExpressionBlocks(expression: ExpressionBlockBase): Any {
        expression.id?.let {
            currentId = it
        }
        val leftSide = expression.leftSide
        val rightSide = expression.rightSide
        val leftSideType: VariableType? = getTypeOfAny(leftSide)
        val rightSideType: VariableType? = getTypeOfAny(rightSide)
        if (leftSideType == VariableType.DOUBLE && VariableType.DOUBLE == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> {
                    return (convertAnyToDouble(
                        leftSide!!
                    ) + convertAnyToDouble(
                        rightSide!!
                    ))
                }
                ExpressionBlockType.MULTIPLY -> return (convertAnyToDouble(leftSide!!) * convertAnyToDouble(
                    rightSide!!
                ))
                ExpressionBlockType.DIVIDE -> {
                    if (convertAnyToDouble(
                            rightSide!!
                        ) != 0.0
                    ) {
                        return (convertAnyToDouble(leftSide!!) / convertAnyToDouble(
                            rightSide
                        ))
                    } else {
                        throw InterpreterException(currentId, ExceptionType.DIVISION_BY_ZERO)
                    }
                }
                ExpressionBlockType.MINUS -> return (convertAnyToDouble(leftSide!!) - convertAnyToDouble(
                    rightSide!!
                ))
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> throw expression.id?.let {
                    InterpreterException(
                        it, ExceptionType.WRONG_OPERAND_USE_CASE
                    )
                }!!
            }
        }
        if (leftSideType == VariableType.INT && VariableType.INT == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> return (convertAnyToInt(leftSide!!) + convertAnyToInt(
                    rightSide!!
                ))
                ExpressionBlockType.MULTIPLY -> return (convertAnyToInt(leftSide!!) * convertAnyToInt(
                    rightSide!!
                ))
                ExpressionBlockType.DIVIDE -> {
                    if (convertAnyToInt(
                            rightSide!!
                        ) != 0
                    ) {
                        return (convertAnyToInt(leftSide!!) / convertAnyToInt(
                            rightSide
                        ))
                    } else {
                        throw InterpreterException(currentId, ExceptionType.DIVISION_BY_ZERO)
                    }
                }
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> {
                    if (convertAnyToInt(
                            rightSide!!
                        ) != 0
                    ) {
                        return (convertAnyToInt(leftSide!!) % convertAnyToInt(
                            rightSide
                        ))
                    } else {
                        throw InterpreterException(currentId, ExceptionType.DIVISION_BY_ZERO)
                    }
                }
                ExpressionBlockType.MINUS -> return (convertAnyToInt(leftSide!!) - convertAnyToInt(
                    rightSide!!
                ))
            }
        }
        if (leftSideType == VariableType.STRING && VariableType.STRING == rightSideType && expression.expressionBlockType == ExpressionBlockType.PLUS) {
            return '"' + convertAnyToString(leftSide!!) + convertAnyToString(
                rightSide!!
            ) + '"'
        }
        if (leftSideType == null || rightSideType == null) {
            throw InterpreterException(currentId, ExceptionType.LACK_OF_ARGUMENTS)
        } else {
            throw  expression.id?.let { InterpreterException(it, ExceptionType.TYPE_MISMATCH) }!!
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun interpretIOBlocks(IO: IOBlockBase): String? {
        IO.id?.let {
            currentId = it
        }

        when (IO.ioBlockType) {
            IOBlockType.WRITE -> {
                when (IO.argument) {
                    is BlockBase -> {
                        consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(
                            interpretBlock(IO.argument as BlockBase).toString()
                        )
                    }
                    is String -> {
                        val argument = IO.argument as String

                        if (isVariable(argument)) {
                            val variable = heapUseCases.getVariableUseCase.getVariable(argument)
                                ?: throw InterpreterException(
                                    currentId,
                                    ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                                )

                            if (variable.value != null)
                                consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(variable.value.toString())
                            else
                                consoleUseCases.writeToConsoleUseCase.writeOutputToConsole("Undefined")
                        } else {
                            consoleUseCases.writeToConsoleUseCase.writeOutputToConsole(
                                argument.drop(1).dropLast(1)
                            )
                        }
                    }
                    else -> {
                        throw InterpreterException(currentId, ExceptionType.INVALID_BLOCK)
                    }
                }

                return null
            }

            IOBlockType.READ -> {
                val input = consoleUseCases.readFromConsoleUseCase.readFromConsole()
                ioBlocksValues[IO.id as Int] = input
                return input
            }
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
        }
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToDouble(value: Any): Double {
        when (value) {
            is Double -> return value
            is ExpressionBlock -> return interpretExpressionBlocks(value) as Double
            is String -> {
                if (value.toDoubleOrNull() is Double) {
                    return value.toDouble()
                } else if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        when {
                            foundedStoredVariable.type == VariableType.DOUBLE && foundedStoredVariable.value != null -> return foundedStoredVariable.value.toString()
                                .toDouble()
                            foundedStoredVariable.value != null -> throw InterpreterException(
                                currentId,
                                ExceptionType.TYPE_MISMATCH
                            )
                            else -> throw InterpreterException(
                                currentId,
                                ExceptionType.TYPE_MISMATCH
                            )
                        }
                    }
                } else {
                    throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                }
            }
            is IOBlockBase -> {
                if (ioBlocksValues[value.id] != null) {
                    return convertAnyToDouble(ioBlocksValues[value.id]!!)
                } else {
                    return interpretIOBlocks(value)?.let { convertAnyToDouble(it) }!!
                }
            }
        }
        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToInt(value: Any): Int {
        when (value) {
            is Int -> return value
            is Double -> throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
            is ExpressionBlock -> if (getTypeOfAny(value) == VariableType.INT) return interpretExpressionBlocks(
                value
            ) as Int else {
                throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
            }
            is IOBlockBase -> {
                if (ioBlocksValues[value.id] != null) {
                    return convertAnyToInt(ioBlocksValues[value.id]!!)
                } else {
                    return interpretIOBlocks(value)?.let { convertAnyToInt(it) }!!
                }
            }
            is String -> {
                if (value.toIntOrNull() is Int) {
                    return value.toInt()
                } else if (!(value[0] == '"' && value[value.lastIndex] == '"')) {

                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        when {
                            foundedStoredVariable.type == VariableType.INT && foundedStoredVariable.value != null -> return foundedStoredVariable.value.toString()
                                .toInt()
                            foundedStoredVariable.value != null -> throw InterpreterException(
                                currentId,
                                ExceptionType.TYPE_MISMATCH
                            )
                            else -> {
                                throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                            }
                        }
                    }
                } else {
                    throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                }
            }

        }
        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToBoolean(value: Any): Boolean {
        when (value) {
            is Boolean -> return value
            is ConditionBlock -> {
                return interpretConditionBlocks(value)
            }
            is ExpressionBlock -> {
                if (getTypeOfAny(value) == VariableType.BOOLEAN) return interpretExpressionBlocks(
                    value
                ) as Boolean else {
                    throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                }
            }
            is String -> {
                if (value.toBooleanStrictOrNull() is Boolean) {
                    return value.toBooleanStrict()
                }
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        when {
                            foundedStoredVariable.type == VariableType.BOOLEAN && foundedStoredVariable.value != null -> return foundedStoredVariable.value.toString()
                                .toBoolean()
                            foundedStoredVariable.value != null -> throw InterpreterException(
                                currentId,
                                ExceptionType.TYPE_MISMATCH
                            )
                            else -> throw InterpreterException(
                                currentId,
                                ExceptionType.VARIABLE_VALUE_IS_NULL
                            )
                        }
                    }
                } else {
                    throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)

                }
            }
            is IOBlockBase -> {
                if (ioBlocksValues[value.id] != null) {
                    return convertAnyToBoolean(ioBlocksValues[value.id]!!)
                } else {
                    return interpretIOBlocks(value)?.let { convertAnyToBoolean(it) }!!
                }
            }
        }

        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun convertAnyToString(value: Any): String {
        when (value) {
            is ExpressionBlock -> {
                return if (getTypeOfAny(value) == VariableType.STRING) interpretExpressionBlocks(
                    value
                ) as String else throw InterpreterException(
                    currentId,
                    ExceptionType.TYPE_MISMATCH
                )
            }
            is String -> {
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val foundedStoredVariable =
                        heapUseCases.getVariableUseCase.getVariable(value)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        when {
                            foundedStoredVariable.type == VariableType.STRING && foundedStoredVariable.value != null -> return foundedStoredVariable.value as String
                            foundedStoredVariable.value != null -> throw InterpreterException(
                                currentId,
                                ExceptionType.TYPE_MISMATCH
                            )
                            else -> throw InterpreterException(
                                currentId,
                                ExceptionType.VARIABLE_VALUE_IS_NULL
                            )
                        }
                    }
                } else {
                    return value.drop(1).dropLast(1)
                }
            }
            is IOBlockBase -> {
                if (ioBlocksValues[value.id] != null) {
                    return convertAnyToString(ioBlocksValues[value.id]!!)
                } else {
                    return interpretIOBlocks(value)?.let { convertAnyToString(it) }!!
                }
            }
        }
        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private suspend fun getTypeOfAny(value: Any?): VariableType? {
        when (value) {
            is Boolean -> return VariableType.BOOLEAN
            is String -> {
                when {
                    value.toIntOrNull() is Int -> {
                        return VariableType.INT
                    }
                    value.toDoubleOrNull() is Double -> return VariableType.DOUBLE
                    value.toBooleanStrictOrNull() is Boolean -> return VariableType.BOOLEAN
                    else -> {
                        if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                            val foundedStoredVariable =
                                heapUseCases.getVariableUseCase.getVariable(value)

                            if (foundedStoredVariable == null) {
                                throw InterpreterException(
                                    currentId,
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
                if (ioBlocksValues[value.id] != null) {
                    return getTypeOfAny(ioBlocksValues[value.id]!!)
                } else {
                    return interpretIOBlocks(value)?.let { getTypeOfAny(it) }!!
                }
            }
            is Double -> return VariableType.DOUBLE
            is Int -> return VariableType.INT
            is ExpressionBlockBase -> return getTypeOfAny(interpretExpressionBlocks(value))
            is VariableBlock -> return value.variableParams?.type
            is ConditionBlock -> {
                return getTypeOfAny(interpretConditionBlocks(value))
            }
        }
        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
    }

    @Throws(InterpreterException::class)
    private fun isVariable(value: String): Boolean {
        if (value.startsWith('"')) {
            if (value.length > 1) {
                if (value.endsWith('"')) {
                    return false
                }
            }

            throw InterpreterException(currentId, ExceptionType.INVALID_STRING)
        }

        return true
    }


    private fun tryToConvertString(value: String, type: VariableType): Any? {
        //TODO: array
        return when (type) {
            VariableType.BOOLEAN -> value.toBooleanStrictOrNull()
            VariableType.INT -> value.toIntOrNull()
            VariableType.DOUBLE -> value.toDoubleOrNull()
            VariableType.STRING -> value
        }
    }
}



