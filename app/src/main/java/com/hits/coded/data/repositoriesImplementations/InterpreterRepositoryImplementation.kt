package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.codeBlocks.dataClasses.*
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalOperatorType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalOperatorType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.types.VariableType
import com.hits.coded.data.modules.HeapModule
import com.hits.coded.domain.repositories.HeapRepository
import com.hits.coded.domain.repositories.InterpreterRepository
import com.hits.coded.domain.useCases.heap.ReAssignVariableUseCase
import com.hits.coded.domain.useCases.interpreter.InterpreteVariableBlockUseCase

class InterpreterRepositoryImplementation : InterpreterRepository() {
    override fun InterpreteConditionBlocks(condition: ConditionBlock) {
        var ConditionIsTrue: Boolean = false
        if (condition.logicalOperator != null) {
            when (condition.logicalOperator.logicalOperatorType) {

                LogicalOperatorType.AND -> if (condition.rightSide != null) {
                    ConditionIsTrue =
                        (convertAnyToBoolean(condition.leftSide) as Boolean && convertAnyToBoolean(
                            condition.rightSide
                        ) as Boolean)
                }
                LogicalOperatorType.NOT -> ConditionIsTrue =
                    !(convertAnyToBoolean(condition.leftSide) as Boolean)

                LogicalOperatorType.OR -> if (condition.rightSide != null) {
                    ConditionIsTrue =
                        (convertAnyToBoolean(condition.leftSide) as Boolean || convertAnyToBoolean(
                            condition.rightSide
                        ) as Boolean)
                }
            }
        } else {
            if (condition.rightSide != null) {
                when (condition.mathematicalOperator?.mathematicalOperatorType) {
                    MathematicalOperatorType.EQUAL -> ConditionIsTrue =
                        (convertAnyToDouble(condition.leftSide) == convertAnyToDouble(condition.rightSide))
                    MathematicalOperatorType.GREATER_OR_EQUAL_THAN -> ConditionIsTrue =
                        (convertAnyToDouble(condition.leftSide) as Double >= convertAnyToDouble(
                            condition.rightSide
                        ) as Double)
                    MathematicalOperatorType.GREATER_THAN -> ConditionIsTrue =
                        (convertAnyToDouble(condition.leftSide) as Double > convertAnyToDouble(
                            condition.rightSide
                        ) as Double)
                    MathematicalOperatorType.LOWER_OR_EQUAL_THAN -> ConditionIsTrue =
                        (convertAnyToDouble(condition.leftSide) as Double <= convertAnyToDouble(
                            condition.rightSide
                        ) as Double)
                    MathematicalOperatorType.LOWER_THAN -> ConditionIsTrue =
                        (convertAnyToDouble(condition.rightSide) as Double > convertAnyToDouble(
                            condition.leftSide
                        ) as Double)
                    MathematicalOperatorType.NON_EQUAL -> ConditionIsTrue =
                        (convertAnyToDouble(condition.leftSide) != convertAnyToDouble(condition.rightSide))
                }
            }
        }//стоит вынести в отдельную функцию
        if (ConditionIsTrue) {
            if (condition.nestedBlocks != null) {
                for (nestedBlock in condition.nestedBlocks) {
                    when (nestedBlock.type) {
                        BlockType.VARIABLE -> InterpreteVariableBlocks(nestedBlock as VariableBlock)
                        BlockType.CONDITION -> InterpreteConditionBlocks(nestedBlock as ConditionBlock)
                        BlockType.LOOP -> InterpreteLoopBlocks(nestedBlock as LoopBlock)
                        BlockType.EXPRESSION -> InterpreteExpressionBlocks(nestedBlock as ExpressionBlock)
                    }
                }
            }
        }
    }

    override fun InterpreteLoopBlocks(loop: LoopBlock) {
        if (loop.nestedBlocks != null) {
            //while (function(loop))
            for (nestedBlock in loop.nestedBlocks) {
                when (nestedBlock.type) {
                    BlockType.VARIABLE -> InterpreteVariableBlocks(nestedBlock as VariableBlock)
                    BlockType.CONDITION -> InterpreteConditionBlocks(nestedBlock as ConditionBlock)
                    BlockType.LOOP -> InterpreteLoopBlocks(nestedBlock as LoopBlock)
                    BlockType.EXPRESSION -> InterpreteExpressionBlocks(nestedBlock as ExpressionBlock)
                }
            }
        }
    }

    override fun InterpreteVariableBlocks(variable: VariableBlock): StoredVariable? {
        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_CHANGE ->
                variable.variableToChange?.let {
                    HeapRepositoryImplementation().reAssignVariable(
                        it,
                        variable.variableParams
                    )
                }

            VariableBlockType.VARIABLE_GET -> return HeapRepositoryImplementation().getVariable(
                variable.variableParams.name
            )
            VariableBlockType.VARIABLE_SET -> HeapRepositoryImplementation().addVariable(variable.variableParams)
        }
        return null
    }

    override fun InterpreteExpressionBlocks(expression: ExpressionBlock): Any {
        val leftSideType: VariableType? = getTypeOfAny(expression.leftSide)
        val rightSideType: VariableType? = getTypeOfAny(expression.rightSide)
        if (leftSideType == VariableType.DOUBLE && VariableType.DOUBLE == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> return (convertAnyToDouble(expression.leftSide) as Double + convertAnyToDouble(
                    expression.rightSide
                ) as Double)
                ExpressionBlockType.MULTIPLY -> return (convertAnyToDouble(expression.leftSide) as Double * convertAnyToDouble(
                    expression.rightSide
                ) as Double)
                ExpressionBlockType.DIVIDE -> return (convertAnyToDouble(expression.leftSide) as Double / convertAnyToDouble(
                    expression.rightSide
                ) as Double)
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> return (convertAnyToDouble(expression.leftSide) as Double % convertAnyToDouble(
                    expression.rightSide
                ) as Double)
                ExpressionBlockType.MINUS -> return (convertAnyToDouble(expression.leftSide) as Double - convertAnyToDouble(
                    expression.rightSide
                ) as Double)
            }
        }
        if (leftSideType == VariableType.INT && VariableType.INT == rightSideType) {
            when (expression.expressionBlockType) {
                ExpressionBlockType.PLUS -> return (convertAnyToInt(expression.leftSide) as Int + convertAnyToInt(
                    expression.rightSide
                ) as Int)
                ExpressionBlockType.MULTIPLY -> return (convertAnyToInt(expression.leftSide) as Int * convertAnyToInt(
                    expression.rightSide
                ) as Int)
                ExpressionBlockType.DIVIDE -> return (convertAnyToInt(expression.leftSide) as Int / convertAnyToInt(
                    expression.rightSide
                ) as Double)
                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> return (convertAnyToInt(expression.leftSide) as Int % convertAnyToInt(
                    expression.rightSide
                ) as Int)
                ExpressionBlockType.MINUS -> return (convertAnyToInt(expression.leftSide) as Int - convertAnyToInt(
                    expression.rightSide
                ) as Int)
            }
        }
        if (leftSideType == VariableType.STRING && VariableType.STRING == rightSideType && expression.expressionBlockType == ExpressionBlockType.PLUS) {
            return convertAnyToString(expression.leftSide) as String + convertAnyToString(expression.rightSide) as String
        }
        return 0
    }

    override fun InterpreteIOBlocks(IO: IOBlock) {
        //IO action function
    }

    private fun convertAnyToDouble(value: Any): Double? {
        when (value) {
            is Double -> return value
            is ExpressionBlock -> return InterpreteExpressionBlocks(value) as Double
            is VariableBlock -> return InterpreteVariableBlocks(value)!!.value as Double
            is String -> return value.toDouble()
        }
        return null
    }

    private fun convertAnyToInt(value: Any): Int? {
        when (value) {
            is Double -> return value.toInt()
            is Int -> return value
            is ExpressionBlock -> return InterpreteExpressionBlocks(value) as Int
            is VariableBlock -> return InterpreteVariableBlocks(value)!!.value as Int
            is String -> return value.toInt()
        }
        return null
    }

    private fun convertAnyToBoolean(value: Any): Boolean? {
        when (value) {
            is Boolean -> return value
            is ExpressionBlock -> return InterpreteExpressionBlocks(value) as Boolean
            is VariableBlock -> return InterpreteVariableBlocks(value)!!.value as Boolean
            is String -> return value.toBoolean()
        }
        return null
    }

    private fun convertAnyToString(value: Any): String? {
        when (value) {
            is ExpressionBlock -> return if (getTypeOfAny(value)==VariableType.STRING) InterpreteExpressionBlocks(value) as String else null
            is VariableBlock -> return if(value.variableParams.type==VariableType.STRING) InterpreteVariableBlocks(value)!!.value as String else null
            is String -> return value
        }
        return null
    }

    private fun getTypeOfAny(value: Any): VariableType? {
        when (value) {
            is String -> return VariableType.STRING
            is Double -> return VariableType.DOUBLE
            is Int -> return VariableType.INT
            is ExpressionBlock -> return getTypeOfAny(InterpreteExpressionBlocks(value))
            is VariableBlock -> return getTypeOfAny(InterpreteVariableBlocks(value)!!.value)
        }
        return null
    }
}
