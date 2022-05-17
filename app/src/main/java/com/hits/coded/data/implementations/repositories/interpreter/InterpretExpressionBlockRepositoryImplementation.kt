package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretExpressionBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretExpressionBlockRepositoryImplementation
@Inject constructor(
    private val interpreterAuxiliaryUseCases: InterpreterAuxiliaryUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases
) :
    InterpretExpressionBlockRepository() {
    override suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase): Any {
        expressionBlock.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        val leftSide = interpreterAuxiliaryUseCases.getBaseTypeUseCase.getBaseType(
            expressionBlock.leftSide,
            true
        )
        val rightSide = interpreterAuxiliaryUseCases.getBaseTypeUseCase.getBaseType(
            expressionBlock.rightSide,
            true
        )

        if (leftSide is Number && rightSide is Number) {

            val leftSideUnwrapped = leftSide.toDouble()
            val rightSideUnwrapped = rightSide.toDouble()

            return when (expressionBlock.expressionBlockType) {
                ExpressionBlockType.PLUS -> leftSideUnwrapped + rightSideUnwrapped

                ExpressionBlockType.MULTIPLY -> leftSideUnwrapped * rightSideUnwrapped

                ExpressionBlockType.DIVIDE -> {
                    if (rightSideUnwrapped == 0.0) {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }

                    leftSideUnwrapped / rightSideUnwrapped
                }

                ExpressionBlockType.MINUS -> leftSideUnwrapped - rightSideUnwrapped

                ExpressionBlockType.DIVIDE_WITH_REMAINDER -> {
                    if (rightSideUnwrapped == 0.0) {
                        throw InterpreterException(ExceptionType.DIVISION_BY_ZERO)
                    }

                    leftSideUnwrapped % rightSideUnwrapped
                }

                null -> throw InterpreterException(ExceptionType.LACK_OF_ARGUMENTS)
            }
        }

        if (leftSide is String &&
            rightSide is String &&
            expressionBlock.expressionBlockType == ExpressionBlockType.PLUS
        ) {
            return leftSide + rightSide
        }

        throw InterpreterException(ExceptionType.TYPE_MISMATCH)
    }
}