package com.hits.coded.domain.repositories;

import com.hits.coded.data.models.codeBlocks.dataClasses.*

abstract class InterpreterRepository {
    abstract fun InterpreteConditionBlocks(condition:ConditionBlock):Boolean?
    abstract fun InterpreteLoopBlocks(loop: LoopBlock)
    abstract fun InterpreteVariableBlocks(variable:VariableBlock)
    abstract fun InterpreteExpressionBlocks(expression:ExpressionBlock):Any
    abstract fun InterpreteIOBlocks(IO:IOBlock)
}
