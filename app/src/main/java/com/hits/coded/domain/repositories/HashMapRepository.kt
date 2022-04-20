package com.hits.coded.domain.repositories

import com.hits.coded.data.models.HashMap.StoredVariable
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType

abstract class HashMapModule {
    abstract val hashMap:HashMap<String,StoredVariable>
    abstract fun addVariable(variableName:String, type:VariableType,length:Int)
    abstract fun findVariable(variableName:String):StoredVariable
}