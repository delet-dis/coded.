package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.condition.ConditionBlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.LoopBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.dataClasses.condition.ConditionBlock
import com.hits.coded.data.models.codeBlocks.types.BlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalBlockType
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreter.useCases.ConvertorsUseCases
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.data.models.sharedTypes.ExceptionType
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretConditionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConditionInterpreter
@Inject constructor(
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases,
    private val convertorsUseCases: ConvertorsUseCases,
    private val currentIdValue: CurrentIdValue
) :
    InterpretConditionRepository() {
    override suspend fun interpretConditionBlocks(
        conditionBlock: ConditionBlockBase
    ): Boolean {
        conditionBlock.id?.let {
            currentIdValue.currentId = it
        }

        var conditionIsTrue = false

        val leftSide =
            auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(conditionBlock.leftSide, true)

        var rightSide: Any? = null

        if (conditionBlock.logicalBlock?.logicalBlockType != LogicalBlockType.NOT) {
            rightSide = auxiliaryFunctionsUseCases.getBaseTypeUseCase.getBaseType(
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
                    convertorsUseCases.convertAnyToStringUseCase.convertAnyToString(conditionBlock.rightSide!!)
                )

                is Number -> leftSide.toDouble().compareTo(
                    convertorsUseCases.convertAnyToDoubleUseCase.convertAnyToDouble(conditionBlock.rightSide!!)
                )

                is Boolean -> leftSide.compareTo(
                    convertorsUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(conditionBlock.rightSide!!)
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
                    auxiliaryFunctionsUseCases.interpretBlocksUseCase.interpretBlock(blockBase)
                }
            }
        }
        return conditionIsTrue
    }
}