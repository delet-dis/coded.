package com.hits.coded.domain.useCases.heap

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository

class ReAssignVariableUseCase(private val heapRepository: HeapRepository) {
    suspend fun reAssignVariable(variableName: String, newValue: StoredVariable) =
        heapRepository.reAssignVariable(variableName, newValue)
}