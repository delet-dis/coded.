package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeapRepositoryImplementation @Inject constructor() : HeapRepository() {
    private val hashMap: HashMap<String, StoredVariable?> = HashMap()

    override fun addVariable(storedVariable: StoredVariable) {
        storedVariable.name?.let {
            hashMap[it] = storedVariable
        }
    }

    override fun getVariable(variableName: String): StoredVariable? {
        return hashMap[variableName]
    }

    override fun reAssignVariable(
        variableName: String,
        newValue: Any
    ) {
        hashMap[variableName]?.let {
            hashMap[variableName]?.value = newValue
        }
    }

    override fun deleteVariable(variableName: String) {
        hashMap.remove(variableName)
    }

    override fun isVariableDeclared(variableName: String): Boolean {
        return hashMap.containsKey(variableName)
    }

    override fun clear() {
        hashMap.clear()
    }
}