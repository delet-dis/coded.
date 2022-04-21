package com.hits.coded.domain.repositories

import com.hits.coded.data.models.heap.dataClasses.StoredVariable

abstract class HeapRepository {
    abstract val hashMap: HashMap<String, StoredVariable>
    abstract fun addVariable(variableToAdd: StoredVariable)
    abstract fun getVariable(variableName: String): StoredVariable?
    abstract fun reAssignVariable(variableToReAssign: StoredVariable, newVariable: StoredVariable)
}