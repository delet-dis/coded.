package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.VariableBlockBase
import com.hits.coded.domain.repositories.interpreterRepositories.InterpretVariableBlockRepository

class InterpretVariableBlockUseCase(private val interpretVariableBlockRepository: InterpretVariableBlockRepository) {
    suspend fun interpretVariableBlocks(variable: VariableBlockBase) =
        interpretVariableBlockRepository.interpretVariableBlocks(variable)
}