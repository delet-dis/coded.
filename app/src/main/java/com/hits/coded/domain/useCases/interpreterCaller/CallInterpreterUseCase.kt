package com.hits.coded.domain.useCases.interpreterCaller

import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.domain.repositories.InterpreterCallerRepository

class CallInterpreterUseCase(
    private val interpreterCallerRepository: InterpreterCallerRepository
) {
    suspend fun callInterpreter(start: StartBlock) =
        interpreterCallerRepository.callInterpreter(start)
}