package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class GetVariableUseCase(private val heapRepository: HeapRepository) {
    fun getVariable(variableName: String) = heapRepository.getVariable(variableName)
}