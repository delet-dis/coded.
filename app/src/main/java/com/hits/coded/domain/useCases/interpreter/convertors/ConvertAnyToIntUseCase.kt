package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository

class ConvertAnyToIntUseCase(private val convertorsRepository: ConvertorsRepository) {
    suspend fun convertAnyToInt(value: Any?)=convertorsRepository.convertAnyToInt(value)
}