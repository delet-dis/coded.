package com.hits.coded.domain.useCases.heap

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository

class AddVariableUseCase(
    private val heapRepository: HeapRepository
) {
    fun addVariable(variableToAdd: StoredVariable) =
        heapRepository.addVariable(variableToAdd)
}