package com.hits.coded.domain.repositories

import com.hits.coded.data.models.codeBlocks.dataClasses.ConditionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException

abstract class InterpreterRepository {
    @Throws(InterpreterException::class)
    abstract suspend fun interpretStartBlock(start: StartBlock)

    @Throws(InterpreterException::class)
    abstract suspend fun interpretConditionBlocks(condition: ConditionBlock): Boolean

    @Throws(InterpreterException::class)
    abstract suspend fun interpretLoopBlocks(loop: LoopBlock)

    @Throws(InterpreterException::class)
    abstract suspend fun interpretVariableBlocks(variable: VariableBlock)

    @Throws(InterpreterException::class)
    abstract suspend fun interpretExpressionBlocks(expression: ExpressionBlock): Any

    @Throws(InterpreterException::class)
    abstract suspend fun interpretIOBlocks(IO: IOBlock)
}
