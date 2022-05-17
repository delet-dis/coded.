package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository

class ConvertAnyToIntUseCase(private val interpreterConverterRepository: InterpreterConverterRepository) {
    suspend fun convertAnyToInt(value: Any?) = interpreterConverterRepository.convertAnyToInt(value)
}