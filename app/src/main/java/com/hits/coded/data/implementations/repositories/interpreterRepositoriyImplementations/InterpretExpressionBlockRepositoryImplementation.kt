package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ExpressionBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretExpressionBlockRepository
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretIfBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretExpressionBlockRepositoryImplementation
@Inject constructor(
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases,
    private val currentIdValue: CurrentIdValue
) :
    InterpretExpressionBlockRepository() {
    override suspend fun interpretExpressionBlocks(expressionBlock: ExpressionBlockBase): Any {
        expressionBlock.id?.let {
            currentIdValue.currentId = it
        }

        val leftSide = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(
            expressionBlock.leftSide,
            true
        )
        val rightSide = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(
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