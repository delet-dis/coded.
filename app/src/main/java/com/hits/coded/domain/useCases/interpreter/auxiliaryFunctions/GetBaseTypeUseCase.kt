package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.InterpreterRepositories.AuxiliaryFunctionsRepository

class GetBaseTypeUseCase(private val auxiliaryFunctionsRepository: AuxiliaryFunctionsRepository) {
    suspend fun getBaseType(value: Any?, canBeStringField: Boolean)=auxiliaryFunctionsRepository.getBaseType(value,canBeStringField)
}