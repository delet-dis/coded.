package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository

class ConvertAnyToDoubleUseCase(private val convertorsRepository: ConvertorsRepository) {
    suspend fun convertAnyToDouble(value: Any?)=convertorsRepository.convertAnyToDouble(value)
}