package com.hits.coded.domain.useCases.interpreter.convertors

import com.hits.coded.data.models.arrays.bases.ArrayBase
import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository

class ConvertAnyToArrayBaseUseCase(private val interpreterConverterRepository: InterpreterConverterRepository) {
    suspend fun convertAnyToArrayBase(value: Any, array: ArrayBase) =
        interpreterConverterRepository.convertAnyToArrayBase(value, array)
}