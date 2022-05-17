package com.hits.coded.data.implementations.repositories.interpreterRepositoriyImplementations

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.data.models.codeBlocks.bases.subBlocks.LoopBlockBase
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType
import com.hits.coded.data.models.interpreter.useCases.AuxiliaryFunctionsUseCases
import com.hits.coded.data.models.interpreter.useCases.ConvertorsUseCases
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretLoopBlockRepository
import javax.inject.Singleton

@Singleton
class InterpretLoopBlockRepositoryImplementation(
    private val convertorsUseCases: ConvertorsUseCases,
    private val auxiliaryFunctionsUseCases: AuxiliaryFunctionsUseCases,
    private val currentIdValue: CurrentIdValue
) : InterpretLoopBlockRepository() {
    override suspend fun interpretLoopBlock(loopBlock: LoopBlockBase) {
        loopBlock.id?.let {
            currentIdValue.currentId = it
        }

        loopBlock.nestedBlocks?.let {
            while (convertorsUseCases.convertAnyToBooleanUseCase.convertAnyToBoolean(loopBlock.conditionBlock)) {
                it.forEach { blockBase ->
                    auxiliaryFunctionsUseCases.interpretBlocksUseCase.interpretBlock(blockBase)
                }
            }
        }
    }
}