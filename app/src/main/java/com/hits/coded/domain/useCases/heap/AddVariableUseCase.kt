package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class AddVariableUseCase(private val heapRepository: HeapRepository) {
    suspend fun addVariable(variableName: String) =
        heapRepository.addVariable(variableName)
}