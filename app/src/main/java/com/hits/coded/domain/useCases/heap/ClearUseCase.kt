package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class ClearUseCase(private val heapRepository: HeapRepository) {
    fun clear() = heapRepository.clear()
}