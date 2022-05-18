package com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions

import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository

class IsVariableUseCase(private val interpreterAuxiliaryRepository: InterpreterAuxiliaryRepository) {
    fun isVariable(value: String) = interpreterAuxiliaryRepository.isVariable(value)
}