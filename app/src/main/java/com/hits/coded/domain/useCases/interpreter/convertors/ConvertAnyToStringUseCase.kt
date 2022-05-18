package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository

class ConvertAnyToStringUseCase(private val interpreterConverterRepository: InterpreterConverterRepository) {
    suspend fun convertAnyToString(value: Any) =
        interpreterConverterRepository.convertAnyToString(value)
}