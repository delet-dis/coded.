package com.hits.coded.domain.repositories

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import kotlinx.coroutines.flow.Flow

abstract class HeapRepository {
    abstract val hashMap: Flow<HashMap<String, StoredVariable?>>
    abstract suspend fun addVariable(variableName: String)
    abstract fun getVariable(variableName: String): StoredVariable?
    abstract suspend fun reAssignVariable(variableName: String, newValue: Any)
    abstract suspend fun deleteVariable(variableName: String)
}