package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.codeBlocks.dataClasses.ConditionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalOperatorType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalOperatorType
import com.hits.coded.data.models.interpreterException.InterpreterException
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.data.models.types.ExceptionType
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreterRepositoryImplementation : InterpreterRepository() {
    private var currentId:Int=0
    @Throws
     override suspend fun interpreteStartBlock(start: StartBlock) {
        start.id?.let{
            currentId=it
        }

        for (nestedBlock in start.nestedBlocks!!) {
            when (nestedBlock.type) {
                BlockType.VARIABLE -> interpretVariableBlocks(nestedBlock as VariableBlock)
                BlockType.CONDITION -> interpretConditionBlocks(nestedBlock as ConditionBlock)
                BlockType.LOOP -> interpretLoopBlocks(nestedBlock as LoopBlock)
                BlockType.EXPRESSION -> interpretExpressionBlocks(nestedBlock as ExpressionBlock)
                BlockType.START -> throw nestedBlock.id?.let {
                    InterpreterException(
                        it,
                        ExceptionType.WRONG_START_POSITION
                    )
                }!!
                BlockType.IO -> interpretIOBlocks(nestedBlock as IOBlock)
            }
        }
    }
    @Throws
    private suspend fun interpretConditionBlocks(condition: ConditionBlock): Boolean {
        condition.id?.let{
            currentId=it
        }
        var conditionIsTrue = false
        val leftSideType = getTypeOfAny(condition.leftSide)
        val rightSideType = getTypeOfAny(condition.rightSide)
        if (condition.logicalOperator != null) {
            when (condition.logicalOperator.logicalOperatorType) {

                LogicalOperatorType.AND -> {
                    when {
                        condition.rightSide != null && leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN -> {
                            conditionIsTrue =
                                (convertAnyToBoolean(condition.leftSide) && convertAnyToBoolean(
                                    condition.rightSide
                                ))
                        }
                        condition.rightSide != null -> throw condition.id?.let {
                            InterpreterException(
                                it, ExceptionType.TYPE_MISMATCH
                            )
                        }!!
                        else -> throw condition.id?.let {
                            InterpreterException(
                                it,
                                ExceptionType.LACK_OF_ARGUMENTS
                            )
                        }!!
                    }

                }
                LogicalOperatorType.NOT -> {
                    if (leftSideType == VariableType.BOOLEAN) conditionIsTrue =
                        !(convertAnyToBoolean(condition.leftSide))
                    else {
                        throw condition.id?.let {
                            InterpreterException(
                                it,
                                ExceptionType.TYPE_MISMATCH
                            )
                        }!!
                    }
                }
                LogicalOperatorType.OR -> {
                    when {
                        condition.rightSide != null && leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN -> {
                            conditionIsTrue =
                                (convertAnyToBoolean(condition.leftSide) || convertAnyToBoolean(
                                    condition.rightSide
                                ))
                        }
                        condition.rightSide != null -> throw condition.id?.let {
                            InterpreterException(
                                it, ExceptionType.TYPE_MISMATCH
                            )
                        }!!
                        else -> throw condition.id?.let {
                            InterpreterException(
                                it,
                                ExceptionType.LACK_OF_ARGUMENTS
                            )
                        }!!
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
                                        condition.rightSide
                                    ))
                            } else {
                                throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }!!
                            }
                        }
                        MathematicalOperatorType.GREATER_OR_EQUAL_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.leftSide) >= convertAnyToDouble(
                                            condition.rightSide
                                        ))
                                }
                                rightSideType == leftSideType -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1, ExceptionType.WRONG_OPERAND_USE_CASE
                                    )
                                }!!
                                else -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }!!
                            }
                        }
                        MathematicalOperatorType.GREATER_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.leftSide) > convertAnyToDouble(
                                            condition.rightSide
                                        ))
                                }
                                rightSideType == leftSideType -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1, ExceptionType.WRONG_OPERAND_USE_CASE
                                    )
                                }!!
                                else -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }!!
                            }
                        }
                        MathematicalOperatorType.LOWER_OR_EQUAL_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.leftSide) <= convertAnyToDouble(
                                            condition.rightSide
                                        ))
                                }
                                rightSideType == leftSideType -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1, ExceptionType.WRONG_OPERAND_USE_CASE
                                    )
                                }!!
                                else -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }!!
                            }
                        }
                        MathematicalOperatorType.LOWER_THAN -> {
                            when {
                                rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT) -> {
                                    conditionIsTrue =
                                        (convertAnyToDouble(condition.rightSide) > convertAnyToDouble(
                                            condition.leftSide
                                        ))
                                }
                                rightSideType == leftSideType -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1, ExceptionType.WRONG_OPERAND_USE_CASE
                                    )
                                }!!
                                else -> throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }!!
                            }
                        }
                        MathematicalOperatorType.NON_EQUAL -> {
                            if (rightSideType == leftSideType) {
                                conditionIsTrue =
                                    (convertAnyToDouble(condition.leftSide) != convertAnyToDouble(
                                        condition.rightSide
                                    ))
                            } else {
                                throw condition.id?.let { it1 ->
                                    InterpreterException(
                                        it1,
                                        ExceptionType.TYPE_MISMATCH
                                    )
                                }!!
                            }
                        }
                    }
                }
            } else {
                throw condition.id?.let {
                    InterpreterException(
                        it,
                        ExceptionType.LACK_OF_ARGUMENTS
                    )
                }!!
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

    private suspend fun interpretLoopBlocks(loop: LoopBlock) {
        loop.id?.let{
            currentId=it
        }
        if (loop.nestedBlocks != null) {
            while (interpretConditionBlocks(loop.conditionBlock as ConditionBlock)) {
                for (nestedBlock in loop.nestedBlocks!!) {
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
    }

    private suspend fun interpretVariableBlocks(variable: VariableBlock){
        variable.id?.let{
            currentId=it
        }
        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_SET -> {
                when (variable.valueToSet) {
                    is ExpressionBlock -> {
                        val expressionValueType = getTypeOfAny(variable.valueToSet)
                        if (expressionValueType == variable.variableParams?.type) {
                            variable.variableParams?.name?.let {
                                when (expressionValueType) {
                                    VariableType.INT -> {
                                        HeapRepositoryImplementation().reAssignVariable(
                                            it,
                                            convertAnyToInt(variable.valueToSet as ExpressionBlock)
                                        )
                                    }
                                    VariableType.DOUBLE -> {
                                        HeapRepositoryImplementation().reAssignVariable(
                                            it,
                                            convertAnyToDouble(variable.valueToSet as ExpressionBlock)
                                        )
                                    }
                                    VariableType.STRING -> {
                                        HeapRepositoryImplementation().reAssignVariable(
                                            it,
                                            convertAnyToString(variable.valueToSet as ExpressionBlock)
                                        )
                                    }
                                    VariableType.BOOLEAN -> {}
                                    else -> {
                                        throw variable.id?.let { it1 ->
                                            InterpreterException(
                                                it1, ExceptionType.NONEXISTING_DATA_TYPE
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
                        if (!((variable.valueToSet as String)[0] == '"' && (variable.valueToSet as String)[(variable.valueToSet as String).lastIndex] == '"')) {
                            val variableName = (variable.valueToSet as String).drop(1).dropLast(1)
                            val foundedStoredVariable =
                                HeapRepositoryImplementation().getVariable(variableName)
                            if (foundedStoredVariable == null) {
                                throw variable.id?.let {
                                    InterpreterException(
                                        it,
                                        ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                                    )
                                }!!
                            } else {
                                if (foundedStoredVariable.type == variable.variableParams?.type) {
                                    foundedStoredVariable.value?.let {
                                        variable.variableParams?.name?.let { it1 ->
                                            HeapRepositoryImplementation().reAssignVariable(
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
                            when {
                                variable.variableParams?.type == VariableType.STRING -> {
                                    variable.variableParams?.name?.let {
                                        HeapRepositoryImplementation().reAssignVariable(
                                            it,
                                            variable.valueToSet as String
                                        )
                                    }
                                }
                                (variable.valueToSet as String).toIntOrNull() is Int -> {
                                    variable.variableParams?.name?.let {
                                        HeapRepositoryImplementation().reAssignVariable(
                                            it, (variable.valueToSet as String).toInt()
                                        )
                                    }
                                }
                                (variable.valueToSet as String).toDoubleOrNull() is Double -> {
                                    variable.variableParams?.name?.let {
                                        HeapRepositoryImplementation().reAssignVariable(
                                            it, (variable.valueToSet as String).toDouble()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            VariableBlockType.VARIABLE_CHANGE -> {
                val typeOfNewValue = getTypeOfAny(variable.valueToSet)
                if (typeOfNewValue == variable.variableParams?.type) {
                    when (typeOfNewValue) {
                        VariableType.INT -> {
                            val toAdd:Int= variable.valueToSet?.let { convertAnyToInt(it) }!!
                                variable.variableParams?.name?.let {
                                    HeapRepositoryImplementation().reAssignVariable(
                                        it,
                                        (variable.variableParams?.value as String).toInt() + toAdd)
                                }
                        }
                        VariableType.DOUBLE->{
                            val toAdd:Double= variable.valueToSet?.let { convertAnyToDouble(it) }!!
                            variable.variableParams?.name?.let {
                                HeapRepositoryImplementation().reAssignVariable(
                                    it,
                                    (variable.variableParams?.value as String).toDouble() + toAdd)
                            }
                        }
                        else->{throw  variable.id?.let { InterpreterException(it,ExceptionType.TYPE_MISMATCH) }!!
                        }
                    }
                }
                else{
                    throw variable.id?.let { InterpreterException(it,ExceptionType.TYPE_MISMATCH) }!!
                }
            }
            VariableBlockType.VARIABLE_CREATE -> variable.variableParams?.let {
                it.name?.let { it1 ->
                    HeapRepositoryImplementation().addVariable(
                        it1
                    )
                }
            }

            else -> {}
        }
        throw variable.id?.let { InterpreterException(it, ExceptionType.WRONG_OPERAND_USE_CASE) }!!
    }

    private suspend fun interpretExpressionBlocks(expression: ExpressionBlock): Any {
        expression.id?.let{
            currentId=it
        }
        val leftSideType: VariableType? = getTypeOfAny(expression.leftSide)
        val rightSideType: VariableType? = getTypeOfAny(expression.rightSide)
        if (leftSideType == VariableType.DOUBLE && VariableType.DOUBLE == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> return (convertAnyToDouble(expression.leftSide) + convertAnyToDouble(
                    expression.rightSide
                ))
                ExpressionBlockType.MULTIPLY -> return (convertAnyToDouble(expression.leftSide) * convertAnyToDouble(
                    expression.rightSide
                ))
                ExpressionBlockType.DIVIDE -> return (convertAnyToDouble(expression.leftSide) / convertAnyToDouble(
                    expression.rightSide
                ))
                ExpressionBlockType.MINUS -> return (convertAnyToDouble(expression.leftSide) - convertAnyToDouble(
                    expression.rightSide
                ))
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> throw expression.id?.let {
                    InterpreterException(
                        it, ExceptionType.WRONG_OPERAND_USE_CASE
                    )
                }!!
                ExpressionBlockType.BRACKETS -> {}
            }
        }
        if (leftSideType == VariableType.INT && VariableType.INT == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> return (convertAnyToInt(expression.leftSide) + convertAnyToInt(
                    expression.rightSide
                ))
                ExpressionBlockType.MULTIPLY -> return (convertAnyToInt(expression.leftSide) * convertAnyToInt(
                    expression.rightSide
                ))
                ExpressionBlockType.DIVIDE -> return (convertAnyToInt(expression.leftSide) / convertAnyToInt(
                    expression.rightSide
                ))
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> return (convertAnyToInt(expression.leftSide) % convertAnyToInt(
                    expression.rightSide
                ))
                ExpressionBlockType.MINUS -> return (convertAnyToInt(expression.leftSide) - convertAnyToInt(
                    expression.rightSide
                ))
                ExpressionBlockType.BRACKETS -> {}
            }
        }
        if (leftSideType == VariableType.STRING && VariableType.STRING == rightSideType && expression.expressionBlockType == ExpressionBlockType.PLUS) {
            return convertAnyToString(expression.leftSide) + convertAnyToString(expression.rightSide)
        }
        throw  expression.id?.let { InterpreterException(it, ExceptionType.TYPE_MISMATCH) }!!
    }

    private suspend fun interpretIOBlocks(IO: IOBlock) {
        //IO action function
    }

    private suspend fun convertAnyToDouble(value: Any): Double {

        when (value) {
            is Double -> return value
            is ExpressionBlock -> return interpretExpressionBlocks(value) as Double
            is String -> {
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val variableName = value.drop(1).dropLast(1)
                    val foundedStoredVariable =
                        HeapRepositoryImplementation().getVariable(variableName)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        if (foundedStoredVariable.type == VariableType.DOUBLE) {
                            return foundedStoredVariable.value as Double
                        } else {
                            throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                        }
                    }
                } else {
                    if (value.toDoubleOrNull() is Double) {
                        return value.toDouble()
                    } else {
                        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                    }
                }
            }
        }
        throw InterpreterException(currentId, ExceptionType.NONEXISTING_DATA_TYPE)
    }

    private suspend fun convertAnyToInt(value: Any): Int {
        when (value) {
            is Double -> throw InterpreterException(currentId,ExceptionType.TYPE_MISMATCH)
            is Int -> return value
            is ExpressionBlock -> if (getTypeOfAny(value) == VariableType.INT) return interpretExpressionBlocks(
                value
            ) as Int else {
                throw InterpreterException(currentId,ExceptionType.TYPE_MISMATCH)
            }
            is String -> {
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val variableName = value.drop(1).dropLast(1)
                    val foundedStoredVariable =
                        HeapRepositoryImplementation().getVariable(variableName)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        if (foundedStoredVariable.type == VariableType.INT) {
                            return foundedStoredVariable.value as Int
                        } else {
                            throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                        }
                    }
                } else {
                    if (value.toIntOrNull() is Int) {
                        return value.toInt()
                    } else {
                        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                    }
                }
            }
        }
        throw InterpreterException(currentId, ExceptionType.NONEXISTING_DATA_TYPE)
    }

    private suspend fun convertAnyToBoolean(value: Any): Boolean {
        when (value) {
            is Boolean -> return value
            is ExpressionBlock -> {
                if (getTypeOfAny(value) == VariableType.BOOLEAN) return interpretExpressionBlocks(
                    value
                ) as Boolean else {
                    throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                }
            }
            is String -> {
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val variableName = value.drop(1).dropLast(1)
                    val foundedStoredVariable =
                        HeapRepositoryImplementation().getVariable(variableName)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        if (foundedStoredVariable.type == VariableType.BOOLEAN) {
                            return foundedStoredVariable.value as Boolean
                        } else {
                            throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                        }
                    }
                } else {
                    if (value.toBooleanStrictOrNull() is Boolean) {
                        return value.toBooleanStrict()
                    } else {
                        throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                    }
                }
            }
        }
        throw InterpreterException(currentId, ExceptionType.NONEXISTING_DATA_TYPE)
    }

    private suspend fun convertAnyToString(value: Any): String {
        when (value) {
            is ExpressionBlock -> {
                return if (getTypeOfAny(value) == VariableType.STRING) interpretExpressionBlocks(
                    value
                ) as String else throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
            }
            is String -> {
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val variableName = value.drop(1).dropLast(1)
                    val foundedStoredVariable =
                        HeapRepositoryImplementation().getVariable(variableName)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        if (foundedStoredVariable.type == VariableType.STRING) {
                            return foundedStoredVariable.value as String
                        } else {
                            throw InterpreterException(currentId, ExceptionType.TYPE_MISMATCH)
                        }
                    }
                } else {
                    return value
                }
            }
        }
        throw InterpreterException(currentId, ExceptionType.NONEXISTING_DATA_TYPE)
    }

    private suspend fun getTypeOfAny(value: Any?): VariableType? {
        when (value) {
            is String -> {
                if (!(value[0] == '"' && value[value.lastIndex] == '"')) {
                    val variableName = value.drop(1).dropLast(1)
                    val foundedStoredVariable =
                        HeapRepositoryImplementation().getVariable(variableName)
                    if (foundedStoredVariable == null) {
                        throw InterpreterException(
                            currentId,
                            ExceptionType.ACCESSING_A_NONEXISTENT_VARIABLE
                        )
                    } else {
                        return foundedStoredVariable.type
                    }
                } else {
                    return when {
                        value.toIntOrNull() is Int -> VariableType.INT
                        value.toDoubleOrNull() is Double -> VariableType.DOUBLE
                        value.toBooleanStrictOrNull() is Boolean -> VariableType.BOOLEAN
                        else -> VariableType.STRING
                    }
                }
            }
            is Double -> return VariableType.DOUBLE
            is Int -> return VariableType.INT
            is Boolean -> return VariableType.BOOLEAN
            is ExpressionBlock -> return getTypeOfAny(interpretExpressionBlocks(value))
            is VariableBlock -> return value.variableParams?.type
        }
        throw InterpreterException(currentId, ExceptionType.NONEXISTING_DATA_TYPE)
    }
}

