package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.subBlocks.LogicalOperatorBase
import com.hits.coded.data.models.codeBlocks.dataClasses.*
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalOperatorType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalOperatorType
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpreterRepositoryImplementation : InterpreterRepository() {
    override fun InterpreteConditionBlocks(condition: ConditionBlock): Boolean? {
        if(condition.nestedBlocks!=null){
            for(nestedBlock in condition.nestedBlocks){
                when(nestedBlock.type){
                        BlockType.VARIABLE->InterpreteVariableBlocks(nestedBlock as VariableBlock)
                        BlockType.CONDITION->InterpreteConditionBlocks(nestedBlock as ConditionBlock)
                        BlockType.LOOP->InterpreteLoopBlocks(nestedBlock as LoopBlock)
                        BlockType.EXPRESSION->InterpreteExpressionBlocks(nestedBlock as ExpressionBlock)
                }
            }
        }
        if (condition.logicalOperator != null) {
            when (condition.logicalOperator.logicalOperatorType) {
                LogicalOperatorType.AND -> return (condition.leftSide as Boolean && condition.rightSide as Boolean)
                LogicalOperatorType.NOT -> return condition.leftSide as Boolean
                LogicalOperatorType.OR -> return (condition.leftSide as Boolean || condition.rightSide as Boolean)
            }
        } else {
            when (condition.mathematicalOperator?.mathematicalOperatorType) {
                MathematicalOperatorType.EQUAL -> return (condition.leftSide == condition.rightSide)
                MathematicalOperatorType.GREATER_OR_EQUAL_THAN -> return (condition.leftSide as Double >= condition.rightSide as Double)
                MathematicalOperatorType.GREATER_THAN -> return (condition.leftSide as Double > condition.rightSide as Double)
                MathematicalOperatorType.LOWER_OR_EQUAL_THAN -> return (condition.leftSide as Double <= condition.rightSide as Double)
                MathematicalOperatorType.LOWER_THAN -> return (condition.rightSide as Double > condition.leftSide as Double)
                MathematicalOperatorType.NON_EQUAL -> return (condition.leftSide != condition.rightSide)
            }
        }
        return null
    }

    override fun InterpreteLoopBlocks(loop: LoopBlock) {
        TODO("Not yet implemented")
    }

    override fun InterpreteVariableBlocks(variable: VariableBlock) {
        TODO("Not yet implemented")
    }

    override fun InterpreteExpressionBlocks(expression: ExpressionBlock): Any {
        TODO("Not yet implemented")
    }

    override fun InterpreteIOBlocks(IO: IOBlock) {
        TODO("Not yet implemented")
    }
}