package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository

class ConvertAnyToStringUseCase(private val convertorsRepository: ConvertorsRepository) {
    suspend fun convertAnyToString(value: Any)=convertorsRepository.convertAnyToString(value)
}