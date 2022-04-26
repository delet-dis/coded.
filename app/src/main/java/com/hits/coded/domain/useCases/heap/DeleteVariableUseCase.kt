package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class DeleteVariableUseCase(private val heapRepository: HeapRepository) {
    suspend fun deleteVariable(variableName: String) {
        heapRepository.deleteVariable(variableName)
    }
}