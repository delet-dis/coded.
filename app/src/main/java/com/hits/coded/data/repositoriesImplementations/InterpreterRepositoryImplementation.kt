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
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreterRepositoryImplementation : InterpreterRepository() {
    override suspend fun interpreteStartBlock(start: StartBlock) {
        for (nestedBlock in start.nestedBlocks!!) {
            when (nestedBlock.type) {
                BlockType.VARIABLE -> interpreteVariableBlocks(nestedBlock as VariableBlock)
                BlockType.CONDITION -> interpreteConditionBlocks(nestedBlock as ConditionBlock)
                BlockType.LOOP -> interpreteLoopBlocks(nestedBlock as LoopBlock)
                BlockType.EXPRESSION -> interpreteExpressionBlocks(nestedBlock as ExpressionBlock)
            }
        }
        throw Exception()
    }

    @Throws
    override suspend fun interpreteConditionBlocks(condition: ConditionBlock): Boolean {
        var ConditionIsTrue: Boolean = false
        var leftSideType: VariableType? = getTypeOfAny(condition.leftSide)
        var rightSideType: VariableType? = getTypeOfAny(condition.rightSide)
        if (condition.logicalOperator != null) {
            when (condition.logicalOperator.logicalOperatorType) {

                LogicalOperatorType.AND -> if (condition.rightSide != null && leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN) {
                    ConditionIsTrue =
                        (convertAnyToBoolean(condition.leftSide) as Boolean && convertAnyToBoolean(
                            condition.rightSide
                        ) as Boolean)
                } else if (condition.rightSide != null) {
                    throw Exception("Non Boolean type in Condition. Block id:${condition.id}")
                } else {
                    throw Exception("Can't find right side in condition. Block id:${condition.id}")
                }
                LogicalOperatorType.NOT -> if (leftSideType == VariableType.BOOLEAN) ConditionIsTrue =
                    !(convertAnyToBoolean(condition.leftSide) as Boolean)
                else {
                    throw Exception("Non Boolean type in Condition. Block id:${condition.id}")
                }
                LogicalOperatorType.OR -> if (condition.rightSide != null && leftSideType == VariableType.BOOLEAN && rightSideType == VariableType.BOOLEAN) {
                    ConditionIsTrue =
                        (convertAnyToBoolean(condition.leftSide) as Boolean || convertAnyToBoolean(
                            condition.rightSide
                        ) as Boolean)
                } else if (condition.rightSide != null) {
                    throw Exception("Non Boolean type in Condition. Block id:${condition.id}")
                } else {
                    throw Exception("Can't find right side in condition. Block id:${condition.id}")
                }
            }
        } else {
            if (condition.rightSide != null) {
                when (condition.mathematicalOperator?.mathematicalOperatorType) {
                    MathematicalOperatorType.EQUAL -> if (rightSideType == leftSideType) {
                        ConditionIsTrue =
                            (convertAnyToDouble(condition.leftSide) == convertAnyToDouble(condition.rightSide))
                    } else {
                        throw Exception("Can't compare variables of different Types. Block id:${condition.id}")
                    }

                    MathematicalOperatorType.GREATER_OR_EQUAL_THAN -> if (rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT)) {
                        ConditionIsTrue =
                            (convertAnyToDouble(condition.leftSide) as Double >= convertAnyToDouble(
                                condition.rightSide
                            ) as Double)
                    } else if (rightSideType == leftSideType) {
                        throw Exception("Can't use \">=\" operand for $rightSideType. Block id:${condition.id}")
                    } else {
                        throw Exception("Can't compare non equal types $leftSideType and $rightSideType. Block id:${condition.id}")
                    }
                    MathematicalOperatorType.GREATER_THAN -> if (rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT)) {
                        ConditionIsTrue =
                            (convertAnyToDouble(condition.leftSide) as Double > convertAnyToDouble(
                                condition.rightSide
                            ) as Double)
                    } else if (rightSideType == leftSideType) {
                        throw Exception("Can't use \">=\" operand for $rightSideType. Block id:${condition.id}")
                    } else {
                        throw Exception("Can't compare non equal types $leftSideType and $rightSideType. Block id:${condition.id}")
                    }
                    MathematicalOperatorType.LOWER_OR_EQUAL_THAN -> if (rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT)) {
                        ConditionIsTrue =
                            (convertAnyToDouble(condition.leftSide) as Double <= convertAnyToDouble(
                                condition.rightSide
                            ) as Double)
                    } else if (rightSideType == leftSideType) {
                        throw Exception("Can't use \">=\" operand for $rightSideType. Block id:${condition.id}")
                    } else {
                        throw Exception("Can't compare non equal types $leftSideType and $rightSideType. Block id:${condition.id}")
                    }
                    MathematicalOperatorType.LOWER_THAN -> if (rightSideType == leftSideType && (rightSideType == VariableType.DOUBLE || rightSideType == VariableType.INT)) {
                        ConditionIsTrue =
                            (convertAnyToDouble(condition.rightSide) as Double > convertAnyToDouble(
                                condition.leftSide
                            ) as Double)
                    } else if (rightSideType == leftSideType) {
                        throw Exception("Can't use \">=\" operand for $rightSideType. Block id:${condition.id}")
                    } else {
                        throw Exception("Can't compare non equal types $leftSideType and $rightSideType. Block id:${condition.id}")
                    }
                    MathematicalOperatorType.NON_EQUAL -> if (rightSideType == leftSideType) {
                        ConditionIsTrue =
                            (convertAnyToDouble(condition.leftSide) != convertAnyToDouble(condition.rightSide))
                    } else {
                        throw Exception("Can't compare variables of different Types. Block id:${condition.id}")
                    }
                }
            }
        }//стоит вынести в отдельную функцию
        if (ConditionIsTrue) {
            if (condition.nestedBlocks != null) {
                for (nestedBlock in condition.nestedBlocks!!) {
                    when (nestedBlock.type) {
                        BlockType.VARIABLE -> interpreteVariableBlocks(nestedBlock as VariableBlock)
                        BlockType.CONDITION -> interpreteConditionBlocks(nestedBlock as ConditionBlock)
                        BlockType.LOOP -> interpreteLoopBlocks(nestedBlock as LoopBlock)
                        BlockType.EXPRESSION -> interpreteExpressionBlocks(nestedBlock as ExpressionBlock)
                    }
                }
            }
        }
        return ConditionIsTrue
    }

    override suspend fun interpreteLoopBlocks(loop: LoopBlock) {
        if (loop.nestedBlocks != null) {
            while (interpreteConditionBlocks(loop.conditionBlock as ConditionBlock)) {
                for (nestedBlock in loop.nestedBlocks!!) {
                    when (nestedBlock.type) {
                        BlockType.VARIABLE -> interpreteVariableBlocks(nestedBlock as VariableBlock)
                        BlockType.CONDITION -> interpreteConditionBlocks(nestedBlock as ConditionBlock)
                        BlockType.LOOP -> interpreteLoopBlocks(nestedBlock as LoopBlock)
                        BlockType.EXPRESSION -> interpreteExpressionBlocks(nestedBlock as ExpressionBlock)
                    }
                }
            }
        }
    }

    override suspend fun interpreteVariableBlocks(variable: VariableBlock): StoredVariable? {
        when (variable.variableBlockType) {
            VariableBlockType.VARIABLE_CHANGE ->
                variable.variableToChange?.let {
                    HeapRepositoryImplementation().reAssignVariable(
                        it.name,
                        variable.variableParams
                    )
                }

            VariableBlockType.VARIABLE_GET -> return HeapRepositoryImplementation().getVariable(
                variable.variableParams.name
            )
            VariableBlockType.VARIABLE_SET -> HeapRepositoryImplementation().addVariable(variable.variableParams.name)
        }
        return null
    }

    override suspend fun interpreteExpressionBlocks(expression: ExpressionBlock): Any {
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

    override suspend fun interpreteIOBlocks(IO: IOBlock) {
        //IO action function
    }

    private suspend fun convertAnyToDouble(value: Any): Double? {
        when (value) {
            is Double -> return value
            is ExpressionBlock -> return interpreteExpressionBlocks(value) as Double
            is VariableBlock -> return interpreteVariableBlocks(value)!!.value as Double
            is String -> return value.toDouble()
        }
        return null
    }

    private suspend fun convertAnyToInt(value: Any): Int? {
        when (value) {
            is Double -> return value.toInt()
            is Int -> return value
            is ExpressionBlock -> if (getTypeOfAny(value) == VariableType.INT) return interpreteExpressionBlocks(
                value
            ) as Int else {
                throw Exception("Can't interprete Expression as Integer. Block id:${value.id}")
            }
            is VariableBlock -> if (getTypeOfAny(value) == VariableType.INT) {
                return value.variableParams.value as Int
            } else {
                throw Exception("Can't interprete Variable as Integer. Block id:${value.id}")
            }
            is String -> return value.toInt()
        }
        return null
    }

    private suspend fun convertAnyToBoolean(value: Any): Boolean? {
        when (value) {
            is Boolean -> return value
            is ExpressionBlock -> if (getTypeOfAny(value) == VariableType.BOOLEAN) return interpreteExpressionBlocks(
                value
            ) as Boolean else {
                throw Exception("Can't transform type to Boolean")
            }
            is VariableBlock -> return interpreteVariableBlocks(value)!!.value as Boolean
            is String -> return value.toBoolean()
        }
        return null
    }

    private suspend fun convertAnyToString(value: Any): String? {
        when (value) {
            is ExpressionBlock -> return if (getTypeOfAny(value) == VariableType.STRING) interpreteExpressionBlocks(
                value
            ) as String else null
            is VariableBlock -> return if (value.variableParams.type == VariableType.STRING) interpreteVariableBlocks(
                value
            )!!.value as String else null
            is String -> return value
        }
        return null
    }

    private suspend fun getTypeOfAny(value: Any?): VariableType? {
        when (value) {
            is String -> return VariableType.STRING
            is Double -> return VariableType.DOUBLE
            is Int -> return VariableType.INT
            is Boolean -> return VariableType.BOOLEAN
            is ExpressionBlock -> return getTypeOfAny(interpreteExpressionBlocks(value))
            is VariableBlock -> return value.variableParams.type
        }
        return null
    }
}
