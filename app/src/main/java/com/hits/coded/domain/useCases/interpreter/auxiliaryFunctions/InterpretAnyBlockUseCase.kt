package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository

class InterpretAnyBlockUseCase(private val interpreterAuxiliaryRepository: InterpreterAuxiliaryRepository) {
    suspend fun interpretBlock(block: BlockBase) =
        interpreterAuxiliaryRepository.interpretBlock(block)
}