package com.hits.coded.domain.useCases.heap

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository

class ReAssignUseCase(private val heapRepository: HeapRepository) {
    fun reAssign(variableName: String, newValue: StoredVariable)=heapRepository.reAssign(variableName,newValue)
}