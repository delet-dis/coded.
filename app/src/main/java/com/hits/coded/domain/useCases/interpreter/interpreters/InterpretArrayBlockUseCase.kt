package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.ArrayBlockBase
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretArrayBlockRepository

class InterpretArrayBlockUseCase(private val interpretArrayBlockRepository: InterpretArrayBlockRepository) {
    suspend fun interpretArrayBlock(block: ArrayBlockBase)=interpretArrayBlockRepository.interpretArrayBlock(block)
}