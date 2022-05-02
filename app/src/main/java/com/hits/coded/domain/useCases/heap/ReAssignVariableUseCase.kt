package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class ReAssignVariableUseCase(private val heapRepository: HeapRepository) {
    fun reAssignVariable(variableName: String, newValue: Any) =
        heapRepository.reAssignVariable(variableName, newValue)
}