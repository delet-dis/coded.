package com.hits.coded.domain.repositories;

import com.hits.coded.data.models.codeBlocks.dataClasses.*
import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class InterpreterRepository {
    abstract fun InterpreteConditionBlocks(condition:ConditionBlock)
    abstract fun InterpreteLoopBlocks(loop: LoopBlock)
    abstract fun InterpreteVariableBlocks(variable:VariableBlock):StoredVariable?
    abstract fun InterpreteExpressionBlocks(expression:ExpressionBlock):Any
    abstract fun InterpreteIOBlocks(IO:IOBlock)
}
