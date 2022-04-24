package com.hits.coded.domain.repositories

import kotlinx.coroutines.flow.Flow


abstract class ConsoleRepository {
    abstract val buffer: Flow<ArrayDeque<String>>
    abstract val isInputAvailable: Flow<Boolean>
    abstract suspend fun clear()
    abstract suspend fun writeToConsole(input: String)
    abstract suspend fun readFromConsole(): String
}