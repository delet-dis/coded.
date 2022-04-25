package com.hits.coded.domain.repositories;

import com.hits.coded.data.models.codeBlocks.dataClasses.*
import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class InterpreterRepository {
    abstract fun interpreteStartBlock(start:StartBlock)
    abstract fun interpreteConditionBlocks(condition:ConditionBlock):Boolean
    abstract fun interpreteLoopBlocks(loop: LoopBlock)
    abstract fun interpreteVariableBlocks(variable:VariableBlock):StoredVariable?
    abstract fun interpreteExpressionBlocks(expression:ExpressionBlock):Any
    abstract fun interpreteIOBlocks(IO:IOBlock)
}
