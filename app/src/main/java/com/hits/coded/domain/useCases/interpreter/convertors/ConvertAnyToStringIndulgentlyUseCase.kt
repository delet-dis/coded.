package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository

class ConvertAnyToStringIndulgentlyUseCase(private val interpreterConverterRepository: InterpreterConverterRepository) {
    suspend fun convertAnyToStringIndulgently(value: Any) =
        interpreterConverterRepository.convertAnyToStringIndulgently(value)
}