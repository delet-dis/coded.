package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.domain.repositories.InterpreterRepositories.AuxiliaryFunctionsRepository

class InterpretBlocksUseCase(private val auxiliaryFunctionsRepository: AuxiliaryFunctionsRepository) {
    suspend fun interpretBlock(block: BlockBase)=auxiliaryFunctionsRepository.interpretBlock(block)
}