package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class IsVariableDeclaredUseCase(private val heapRepository: HeapRepository) {
    fun isVariableDeclared(variableName: String) = heapRepository.isVariableDeclared(variableName)
}