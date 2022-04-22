package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.codeBlocks.dataClasses.*
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalOperatorType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalOperatorType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.modules.HeapModule
import com.hits.coded.domain.repositories.HeapRepository
import com.hits.coded.domain.repositories.InterpreterRepository
import com.hits.coded.domain.useCases.heap.ReAssignVariableUseCase

class InterpreterRepositoryImplementation : InterpreterRepository() {
    override fun InterpreteConditionBlocks(condition: ConditionBlock) {
        var ConditionIsTrue: Boolean = false
        if (condition.logicalOperator != null) {
            when (condition.logicalOperator.logicalOperatorType) {
                LogicalOperatorType.AND -> ConditionIsTrue =
                    (condition.leftSide as Boolean && condition.rightSide as Boolean)
                LogicalOperatorType.NOT -> ConditionIsTrue = condition.leftSide as Boolean
                LogicalOperatorType.OR -> ConditionIsTrue =
                    (condition.leftSide as Boolean || condition.rightSide as Boolean)
            }
        } else {
            when (condition.mathematicalOperator?.mathematicalOperatorType) {
                MathematicalOperatorType.EQUAL -> ConditionIsTrue =
                    (condition.leftSide == condition.rightSide)
                MathematicalOperatorType.GREATER_OR_EQUAL_THAN -> ConditionIsTrue =
                    (condition.leftSide as Double >= condition.rightSide as Double)
                MathematicalOperatorType.GREATER_THAN -> ConditionIsTrue =
                    (condition.leftSide as Double > condition.rightSide as Double)
                MathematicalOperatorType.LOWER_OR_EQUAL_THAN -> ConditionIsTrue =
                    (condition.leftSide as Double <= condition.rightSide as Double)
                MathematicalOperatorType.LOWER_THAN -> ConditionIsTrue =
                    (condition.rightSide as Double > condition.leftSide as Double)
                MathematicalOperatorType.NON_EQUAL -> ConditionIsTrue =
                    (condition.leftSide != condition.rightSide)
            }
        } //стоит вынести в отдельную функцию
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

            VariableBlockType.VARIABLE_GET -> return HeapRepositoryImplementation().getVariable(variable.variableParams.name)
            VariableBlockType.VARIABLE_SET -> HeapRepositoryImplementation().addVariable(variable.variableParams)
        }
        return null
    }

    override fun InterpreteExpressionBlocks(expression: ExpressionBlock): Any {
        when(expression.expressionBlockType){
            ExpressionBlockType.PLUS-> return (expression.leftSide as Double+expression.rightSide as Double)

        }
        return 0
    }

    override fun InterpreteIOBlocks(IO: IOBlock) {
        //IO action function
    }
}
fun convertAnyToDouble(value:Any):Double? {
    when(value){
        is Double -> return value
        is ExpressionBlock -> return InterpreterRepositoryImplementation().InterpreteExpressionBlocks(value) as Double
        is VariableBlock -> return InterpreterRepositoryImplementation().InterpreteVariableBlocks(value)!!.value as Double
        is String ->return null
    }
    return null
}