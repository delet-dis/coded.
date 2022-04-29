package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.domain.repositories.InterpreterRepository

class InterpretIOBlockUseCase(
    private val interpreterRepository: InterpreterRepository
) {
    suspend fun interpreteIOBlock(IO: IOBlock)=interpreterRepository.interpretIOBlocks(IO)

}