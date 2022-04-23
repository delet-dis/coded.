package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class GetVariablesUseCase(private val heapRepository: HeapRepository) {
    fun getVariables() = heapRepository.hashMap
}