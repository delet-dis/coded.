package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository

class ConvertAnyToBooleanUseCase(private val convertorsRepository: ConvertorsRepository) {
    suspend fun convertAnyToBoolean(value: Any?)=convertorsRepository.convertAnyToBoolean(value)
}