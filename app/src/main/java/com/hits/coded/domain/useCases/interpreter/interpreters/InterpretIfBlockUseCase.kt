package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.IfBlockBase
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretIfBlockRepository

class InterpretIfBlockUseCase(private val interpretIfBlockRepository: InterpretIfBlockRepository) {
    suspend fun interpretIfBlock(ifBlock: IfBlockBase) =
        interpretIfBlockRepository.interpretIfBlock(ifBlock)
}