package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository

class ConvertAnyToDoubleUseCase(private val interpreterConverterRepository: InterpreterConverterRepository) {
    suspend fun convertAnyToDouble(value: Any?) =
        interpreterConverterRepository.convertAnyToDouble(value)
}