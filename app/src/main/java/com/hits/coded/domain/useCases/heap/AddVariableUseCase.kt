package com.hits.coded.domain.useCases.heap
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType
import com.hits.coded.domain.repositories.HeapRepository
class AddVariableUseCase(
    private val heapRepository: HeapRepository
    ) {
    fun addVariable(variableName: String, type: VariableType, isArray: Boolean)=heapRepository.addVariable(variableName,type,isArray)
}