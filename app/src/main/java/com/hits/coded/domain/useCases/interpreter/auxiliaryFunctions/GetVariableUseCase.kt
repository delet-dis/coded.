package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.InterpreterRepositories.AuxiliaryFunctionsRepository

class GetVariableUseCase(private val auxiliaryFunctionsRepository: AuxiliaryFunctionsRepository) {
    suspend fun getVariable(variableIdentifier: Any)=auxiliaryFunctionsRepository.getVariable(variableIdentifier)
}