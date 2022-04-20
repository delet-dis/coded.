package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.hashMap.dataClasses.StoredVariable
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType
import com.hits.coded.domain.repositories.HashMapRepository

class HeapRepositoryImplementation(): HashMapRepository() {
    override val hashMap: HashMap<String, StoredVariable>
    override fun addVariable(variableName: String, type: VariableType) {
        TODO("Not yet implemented")
    }

    override fun findVariable(variableName: String): StoredVariable {
        TODO("Not yet implemented")
    }
}