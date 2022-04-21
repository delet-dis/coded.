package com.hits.coded.data.repositoriesImplementations

import android.os.Build
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType
import com.hits.coded.domain.repositories.HeapRepository

class HeapRepositoryImplementation(): HeapRepository() {
    override var hashMap: HashMap<String, StoredVariable> = HashMap<String, StoredVariable>()
    override fun addVariable(variableName: String, type: VariableType, isArray: Boolean) {
        var newValue: StoredVariable
        when (type) {
            VariableType.STRING -> newValue = StoredVariable(type, isArray, "")
            VariableType.DOUBLE -> newValue = StoredVariable(type, isArray, 0.0)
            VariableType.INT -> newValue = StoredVariable(type, isArray, 0)
        }
        hashMap.put(variableName, newValue)
    }

    override fun findVariable(variableName: String): StoredVariable? {
        return hashMap[variableName]
    }

    override fun reAssign(variableName: String, newValue: StoredVariable) {
        if(hashMap[variableName]!!.type == newValue.type){
         hashMap[variableName]=newValue
        }
    }
}