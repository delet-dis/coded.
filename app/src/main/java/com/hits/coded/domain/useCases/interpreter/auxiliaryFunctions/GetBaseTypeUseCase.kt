package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository

class GetBaseTypeUseCase(private val interpreterAuxiliaryRepository: InterpreterAuxiliaryRepository) {
    suspend fun getBaseType(value: Any?, canBeVariableName: Boolean = true, castToNumber: Boolean = true) =
        interpreterAuxiliaryRepository.getBaseType(value, canBeVariableName, castToNumber)
}