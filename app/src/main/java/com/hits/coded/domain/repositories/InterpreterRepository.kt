package com.hits.coded.domain.repositories;

import com.hits.coded.data.models.codeBlocks.dataClasses.ConditionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class InterpreterRepository {
    abstract suspend fun interpreteStartBlock(start:StartBlock)
    abstract suspend fun interpreteConditionBlocks(condition:ConditionBlock):Boolean
    abstract suspend fun interpreteLoopBlocks(loop: LoopBlock)
    abstract suspend fun interpreteVariableBlocks(variable:VariableBlock):StoredVariable?
    abstract suspend fun interpreteExpressionBlocks(expression:ExpressionBlock):Any
    abstract suspend fun interpreteIOBlocks(IO:IOBlock)
}
