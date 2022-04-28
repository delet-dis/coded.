package com.hits.coded.domain.repositories

import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class HeapRepository {
    abstract fun addVariable(variableName: String)
    abstract fun getVariable(variableName: String): StoredVariable?
    abstract fun reAssignVariable(variableName: String, newValue: Any)
    abstract fun deleteVariable(variableName: String)
}