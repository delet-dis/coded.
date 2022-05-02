package com.hits.coded.domain.repositories

import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class HeapRepository {
    abstract fun addVariable(storedVariable: StoredVariable)
    abstract fun getVariable(variableName: String): StoredVariable?
    abstract fun isVariableDeclared(variableName: String): Boolean
    abstract fun reAssignVariable(variableName: String, newValue: Any)
    abstract fun deleteVariable(variableName: String)
    abstract fun clear()
}