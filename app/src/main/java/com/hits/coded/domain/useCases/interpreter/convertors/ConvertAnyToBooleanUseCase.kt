package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository

class ConvertAnyToBooleanUseCase(private val interpreterConverterRepository: InterpreterConverterRepository) {
    suspend fun convertAnyToBoolean(value: Any?) =
        interpreterConverterRepository.convertAnyToBoolean(value)
}