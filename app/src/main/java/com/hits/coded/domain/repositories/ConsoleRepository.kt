package com.hits.coded.domain.repositories

import com.hits.coded.data.models.console.enums.ConsoleMessageType
import kotlinx.coroutines.flow.Flow


abstract class ConsoleRepository {
//    abstract val buffer: Flow<ArrayDeque<SpannableString>>
    abstract val isInputAvailable: Flow<Boolean>
    abstract fun clear()
    abstract suspend fun flush()
    abstract fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType)
    abstract suspend fun readFromConsole(): String
}