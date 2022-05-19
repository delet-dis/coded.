package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository

class GetVariableUseCase(private val interpreterAuxiliaryRepository: InterpreterAuxiliaryRepository) {
    suspend fun getVariable(variableIdentifier: Any) =
        interpreterAuxiliaryRepository.getVariable(variableIdentifier)
}