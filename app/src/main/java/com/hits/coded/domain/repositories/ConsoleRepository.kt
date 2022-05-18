package com.hits.coded.domain.repositories

import android.text.SpannableStringBuilder
import com.hits.coded.data.models.console.enums.ConsoleMessageType
import kotlinx.coroutines.flow.Flow


abstract class ConsoleRepository {
    abstract val output: Flow<SpannableStringBuilder>
    abstract val isInputAvailable: Flow<Boolean>
    abstract fun clear()
    abstract suspend fun flush()
    abstract fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType)
    abstract suspend fun readFromConsole(): String
}