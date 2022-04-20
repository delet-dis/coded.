package com.hits.coded.domain.repositories

import com.hits.coded.data.models.hashMap.dataClasses.StoredVariable
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType

abstract class HashMapRepository {
    abstract val hashMap:HashMap<String, StoredVariable>
    abstract fun addVariable(variableName:String, type:VariableType)
    abstract fun findVariable(variableName:String): StoredVariable
}