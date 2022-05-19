package com.hits.coded.data.implementations.repositories.interpreter

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalBlockType
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretConditionBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretConditionBlockRepositoryImplementation
@Inject constructor(
    private val interpreterAuxiliaryUseCases: InterpreterAuxiliaryUseCases,
    private val interpreterConverterUseCases: InterpreterConverterUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases
) :
    InterpretConditionBlockRepository() {
    @Throws(InterpreterException::class)
    override suspend fun interpretConditionBlocks(
        conditionBlock: ConditionBlockBase
    ): Boolean {
        conditionBlock.id?.let {
            interpreterHelperUseCases.setCurrentIdVariableUseCase.setCurrentIdVariable(it)
        }

        var conditionIsTrue = false

        val leftSide =
            interpreterAuxiliaryUseCases.getBaseTypeUseCase.getBaseType(
                conditionBlock.leftSide,
                true
            )

        var rightSide: Any? = null

        if (conditionBlock.logicalBlock?.logicalBlockType != LogicalBlockType.NOT) {
            rightSide = interpreterAuxiliaryUseCases.getBaseTypeUseCase.getBaseType(
                conditionBlock.rightSide,
                true
            )
        }

        conditionBlock.logicalBlock?.let {
            if (leftSide !is Boolean)
                throw InterpreterException(ExceptionType.TYPE_MISMATCH)

            when (it.logicalBlockType) {
                LogicalBlockType.AND -> {
                    if (rightSide !is Boolean)
                        throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                    conditionIsTrue = leftSide && rightSide
                }

                LogicalBlockType.OR -> {
                    if (rightSide !is Boolean)
                        throw InterpreterException(ExceptionType.TYPE_MISMATCH)

                    conditionIsTrue = leftSide || rightSide
                }

                LogicalBlockType.NOT -> conditionIsTrue = !leftSide
            }
        }

        conditionBlock.mathematicalBlock?.let {
            val resultOfComparison = when (leftSide) {
                is String -> leftSide.compareTo(
                    interpreterConverterUseCases.convertAnyToStringUseCase.convertAnyToString(
                        rightSide!!
                    )
                )

                is Number -> leftSide.toDouble().compareTo(
                    interpreterConverterUseCases.convertAnyToDoubleUseCase.convertAnyToDouble(
                        rightSide!!
                    )
                )

                is Boolean -> leftSide.compareTo(
                    interpreterConverterUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(
                        rightSide!!
                    )
                )

                else -> throw InterpreterException(ExceptionType.TYPE_MISMATCH)
            }

            conditionIsTrue = when (it.mathematicalBlockType) {
                MathematicalBlockType.EQUAL -> resultOfComparison == 0
                MathematicalBlockType.GREATER_OR_EQUAL_THAN -> resultOfComparison >= 0
                MathematicalBlockType.GREATER_THAN -> resultOfComparison > 0
                MathematicalBlockType.LOWER_OR_EQUAL_THAN -> resultOfComparison <= 0
                MathematicalBlockType.LOWER_THAN -> resultOfComparison < 0
                MathematicalBlockType.NON_EQUAL -> resultOfComparison != 0
            }

        }

        if (conditionIsTrue) {
            conditionBlock.nestedBlocks?.let {
                it.forEach { blockBase ->
                    interpreterAuxiliaryUseCases.interpretAnyBlockUseCase.interpretBlock(blockBase)
                }
            }
        }
        return conditionIsTrue
    }
}