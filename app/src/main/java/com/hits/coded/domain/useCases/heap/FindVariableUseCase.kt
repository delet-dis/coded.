package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class FindVariableUseCase(private val heapRepository: HeapRepository) {
    fun findVariable(variableName: String)=heapRepository.findVariable(variableName)
}