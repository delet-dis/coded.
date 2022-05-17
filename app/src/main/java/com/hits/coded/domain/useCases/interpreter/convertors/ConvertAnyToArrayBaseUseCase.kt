package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.domain.repositories.InterpreterRepositories.ConvertorsRepository

class ConvertAnyToArrayBaseUseCase(private val convertorsRepository: ConvertorsRepository) {
    suspend fun convertAnyToArrayBase(value: Any,array: ArrayBase)=convertorsRepository.convertAnyToArrayBase(value,array)
}