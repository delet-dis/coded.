package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository

class ConvertAnyToStringIndulgentlyUseCase(private val convertorsRepository: ConvertorsRepository) {
    suspend fun convertAnyToStringIndulgently(value: Any)=convertorsRepository.convertAnyToStringIndulgently(value)
}