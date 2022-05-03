package com.hits.coded.domain.useCases.interpreter

import com.hits.coded.domain.repositories.InterpreterRepository

class GetCurrentBlockIdUseCase(
    private val interpreterRepository: InterpreterRepository
) {
    fun getId() = interpreterRepository.currentId
}