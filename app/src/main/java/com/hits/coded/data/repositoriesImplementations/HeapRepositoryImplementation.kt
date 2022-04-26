package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.domain.repositories.HeapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeapRepositoryImplementation @Inject constructor() : HeapRepository() {
    private val _hashMap: MutableStateFlow<HashMap<String, StoredVariable?>> = MutableStateFlow(
        HashMap()
    )
    override val hashMap: Flow<HashMap<String, StoredVariable?>>
        get() = _hashMap

    override suspend fun addVariable(variableName: String) {
        _hashMap.value[variableName] = null
        _hashMap.emit(_hashMap.value)
    }

    override fun getVariable(variableName: String): StoredVariable? {
        return _hashMap.value[variableName]
    }

    override suspend fun reAssignVariable(
        variableName: String,
        newValue: StoredVariable
    ) {
        _hashMap.value[variableName]?.let {
            _hashMap.value[variableName] = newValue
        }

        _hashMap.emit(_hashMap.value)
    }

    override suspend fun deleteVariable(variableName: String) {
        _hashMap.value.remove(variableName)
        _hashMap.emit(_hashMap.value)
    }
}