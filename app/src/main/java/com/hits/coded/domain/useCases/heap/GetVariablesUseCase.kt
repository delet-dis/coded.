package com.hits.coded.domain.useCases.heap

import com.hits.coded.domain.repositories.HeapRepository

class GetVariablesUseCase(private val heapRepository: HeapRepository) {
    val heap = heapRepository.hashMap

//    suspend fun getVariables(): Flow<Array<VariableBlock>> {
//        val blocksToReturn = MutableSharedFlow<Array<VariableBlock>>()
//
//        val blocksToReturnAsArrayList = ArrayList<VariableBlock>()
//
//        suspend {
//            heapRepository.hashMap.collect { hashMap ->
//                hashMap.forEach {
//                    blocksToReturnAsArrayList.add(
//                        VariableBlock(
//                            VariableBlockType.VARIABLE_GET,
//                            it.key,
//                            null,
//                            null,
//                            null
//                        )
//                    )
//                }
//
//                blocksToReturn.emit(
//                    blocksToReturnAsArrayList.toTypedArray()
//                )
//            }
//        }
//
//        return blocksToReturn
//    }
}