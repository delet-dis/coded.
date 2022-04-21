package com.hits.coded.domain.repositories

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableType

abstract class HeapRepository {
    abstract val hashMap:HashMap<String, StoredVariable>
    abstract fun addVariable(variableName:String, type:VariableType, isArray:Boolean)
    abstract fun findVariable(variableName:String): StoredVariable?
    abstract fun reAssign(variableName: String,newValue:StoredVariable)
}