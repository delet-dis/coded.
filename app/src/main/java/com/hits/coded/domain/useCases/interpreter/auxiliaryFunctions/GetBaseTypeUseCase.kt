package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository

class GetBaseTypeUseCase(private val interpreterAuxiliaryRepository: InterpreterAuxiliaryRepository) {
    suspend fun getBaseType(value: Any?, canBeStringField: Boolean) =
        interpreterAuxiliaryRepository.getBaseType(value, canBeStringField)
}