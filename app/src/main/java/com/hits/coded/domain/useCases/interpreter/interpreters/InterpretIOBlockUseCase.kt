package com.hits.coded.domain.useCases.interpreter.interpreters

import com.hits.coded.data.models.codeBlocks.bases.subBlocks.io.IOBlockBase
import com.hits.coded.domain.repositories.InterpreterRepositories.InterpretIOBlockRepository

class InterpretIOBlockUseCase(private val interpretIOBlockRepository: InterpretIOBlockRepository) {
    suspend fun interpretIOBlocks(IO: IOBlockBase)=interpretIOBlockRepository.interpretIOBlocks(IO)
}