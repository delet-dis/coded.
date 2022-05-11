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
    private var ioBlocksValues: HashMap<Int, Any> = HashMap()

    @Throws(InterpreterException::class)
    override suspend fun interpretStartBlock(startBlock: StartBlock) {
        ioBlocksValues.clear()

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
                            throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                        else -> throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
                    }
                }

                LogicalBlockType.NOT -> {
                    if (leftSideType == VariableType.BOOLEAN) {
                        conditionIsTrue = !(convertAnyToBoolean(conditionBlock.leftSide!!))
                    } else {
                        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
                    }
                }
            }
        }

        conditionBlock.mathematicalBlock?.let {
            if (leftSideType == rightSideType || areNumericTypes(leftSideType, rightSideType)) {
                val resultOfComparison = when (leftSideType) {
                    VariableType.STRING -> convertAnyToString(conditionBlock.leftSide!!).compareTo(
                        convertAnyToString(conditionBlock.rightSide!!)
                    )

                    VariableType.DOUBLE, VariableType.INT -> convertAnyToDouble(conditionBlock.leftSide!!).compareTo(
                        convertAnyToDouble(conditionBlock.rightSide!!)
                    )

                    VariableType.BOOLEAN -> convertAnyToBoolean(conditionBlock.leftSide!!).compareTo(
                        convertAnyToBoolean(conditionBlock.rightSide!!)
                    )

                    VariableType.ARRAY -> throw InterpreterException(ExceptionType.WTF) //TODO:
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
                throw InterpreterException(ExceptionType.TYPE_MISMATCH)
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
            while (interpretConditionBlocks(loopBlock.conditionBlock)) {
                it.forEach { blockBase ->
                    when (blockBase.type) {
                        BlockType.VARIABLE -> interpretVariableBlocks(blockBase as VariableBlockBase)
                        BlockType.CONDITION -> interpretConditionBlocks(blockBase as ConditionBlockBase)
                        BlockType.LOOP -> interpretLoopBlocks(blockBase as LoopBlockBase)
                        BlockType.EXPRESSION -> interpretExpressionBlocks(blockBase as ExpressionBlockBase)
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
    private suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase): Any {
        expressionBlock.id?.let {
            currentId = it
        }

        val leftSide = expressionBlock.leftSide
        val rightSide = expressionBlock.rightSide

        val leftSideType = getTypeOfAny(leftSide)
        val rightSideType = getTypeOfAny(rightSide)


        if (areNumericTypes(leftSideType, rightSideType)) {

            val leftSideUnwrapped = convertAnyToDouble(leftSide!!)
            val rightSideUnwrapped = convertAnyToDouble(rightSide!!)

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

        if (leftSideType == VariableType.STRING &&
            rightSideType == VariableType.STRING &&
            expressionBlock.expressionBlockType == ExpressionBlockType.PLUS
        ) {

            return '"' + convertAnyToString(leftSide!!) + convertAnyToString(rightSide!!) + '"'
        }

        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
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
                ioBlocksValues[IO.id as Int] = input
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
            BlockType.ARRAY -> interpretArrayBlock(block as ArrayBlock)
            BlockType.IF -> interpretIfBlock(block as IfBlock)
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
                val result = interpretExpressionBlocks(value)
                if (result is Number)
                    return result.toDouble()
                else
                    throw InterpreterException(ExceptionType.TYPE_MISMATCH)
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
                return if (ioBlocksValues[value.id] != null) {
                    convertAnyToInt(ioBlocksValues[value.id]!!)
                } else {
                    convertAnyToInt(interpretIOBlocks(value))
                }
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
    private suspend fun convertAnyToBoolean(value: Any?): Boolean {
        when (value) {
            is Boolean -> return value

            is ConditionBlock -> {
                return interpretConditionBlocks(value)
            }

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

            is IOBlockBase -> {
                return if (ioBlocksValues[value.id] != null) {
                    convertAnyToBoolean(ioBlocksValues[value.id]!!)
                } else {
                    convertAnyToBoolean(interpretIOBlocks(value))
                }
            }
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
                return if (ioBlocksValues[value.id] != null) {
                    convertAnyToString(ioBlocksValues[value.id]!!)
                } else {
                    interpretIOBlocks(value)?.let { convertAnyToString(it) }!!
                }
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
    private suspend fun getTypeOfAny(value: Any?): VariableType {
        when (value) {
            is Boolean -> return VariableType.BOOLEAN

            is String -> {
                when {
                    value.toIntOrNull() is Int -> return VariableType.INT
                    value.toDoubleOrNull() is Double -> return VariableType.DOUBLE
                    value.toBooleanStrictOrNull() is Boolean -> return VariableType.BOOLEAN
                    else -> {
                        return if (isVariable(value)) {
                            val foundedStoredVariable =
                                heapUseCases.getVariableUseCase.getVariable(value)

                            if (foundedStoredVariable?.type == null) {
                                throw InterpreterException(
                                    ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                                )
                            } else {
                                foundedStoredVariable.type!!
                            }
                        } else {
                            VariableType.STRING
                        }
                    }
                }
            }

            is IOBlockBase -> {
                return if (ioBlocksValues[value.id] != null) {
                    getTypeOfAny(ioBlocksValues[value.id]!!)
                } else {
                    getTypeOfAny(interpretIOBlocks(value))
                }
            }

            is Double -> return VariableType.DOUBLE

            is Int -> return VariableType.INT

            is ExpressionBlockBase -> return getTypeOfAny(interpretExpressionBlocks(value))

            is ConditionBlock -> return getTypeOfAny(interpretConditionBlocks(value))

            else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
        }
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

    private fun areNumericTypes(firstType: VariableType?, secondType: VariableType?): Boolean =
        (firstType == VariableType.DOUBLE || firstType == VariableType.INT) &&
                (secondType == VariableType.DOUBLE || secondType == VariableType.INT)


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