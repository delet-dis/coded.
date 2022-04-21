package com.hits.coded.domain.useCases.heap

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository

class ReAssignVariableUseCase(private val heapRepository: HeapRepository) {
    fun reAssignVariable(variableToReAssign: StoredVariable, newVariable: StoredVariable) =
        heapRepository.reAssignVariable(variableToReAssign, newVariable)
}