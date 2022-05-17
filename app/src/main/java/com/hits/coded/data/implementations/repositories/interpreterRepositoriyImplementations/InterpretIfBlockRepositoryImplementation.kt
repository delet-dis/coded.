package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreter.useCases.ConvertorsUseCases
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretIfBlockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterpretIfBlockRepositoryImplementation
@Inject constructor(
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases,
    private val convertorsUseCases: ConvertorsUseCases,
    private val currentIdValue: CurrentIdValue
) : InterpretIfBlockRepository() {
    override suspend fun interpretIfBlock(ifBlock: IfBlockBase) {
        ifBlock.id?.let {
            currentIdValue.currentId = it
        }

        if (convertorsUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(ifBlock.conditionBlock)) {
            ifBlock.nestedBlocks?.let {
                it.forEach { blockBase ->
                    auxiliaryFunctionsUseCases.interpretBlocksUseCase.interpretBlock(blockBase)
                }
            }
        } else if (ifBlock.ifBlockType == IfBlockType.IF_WITH_ELSE) {
            ifBlock.elseBlocks?.let {
                it.forEach { blockBase ->
                    auxiliaryFunctionsUseCases.interpretBlocksUseCase.interpretBlock(blockBase)
                }
            }
        }
    }
}