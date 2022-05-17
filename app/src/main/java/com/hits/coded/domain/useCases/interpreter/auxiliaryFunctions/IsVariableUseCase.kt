package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.InterpreterRepositories.AuxiliaryFunctionsRepository

class IsVariableUseCase(private val auxiliaryFunctionsRepository: AuxiliaryFunctionsRepository) {
    suspend fun isVariable(value: String)=auxiliaryFunctionsRepository.isVariable(value)
}