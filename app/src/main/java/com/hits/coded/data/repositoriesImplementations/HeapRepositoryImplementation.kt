package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository
import javax.inject.Singleton

@Singleton
class HeapRepositoryImplementation : HeapRepository() {
    override var hashMap: HashMap<String, StoredVariable> = HashMap()

    override fun addVariable(variableToAdd: StoredVariable) {
        hashMap[variableToAdd.name] = variableToAdd
    }

    override fun getVariable(variableName: String): StoredVariable? {
        return hashMap[variableName]
    }

    override fun reAssignVariable(variableToReAssign: StoredVariable, newVariable: StoredVariable) {
        hashMap[variableToReAssign.name]?.let {
            hashMap[variableToReAssign.name] = newVariable
        }
    }
}