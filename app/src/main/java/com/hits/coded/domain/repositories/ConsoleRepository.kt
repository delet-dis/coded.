package com.hits.coded.domain.repositories

import android.text.SpannableString
import com.hits.coded.data.models.console.enums.ConsoleMessageType
import kotlinx.coroutines.flow.Flow


abstract class ConsoleRepository {
    abstract val buffer: Flow<ArrayDeque<SpannableString>>
    abstract val isInputAvailable: Flow<Boolean>
    abstract fun clear()
    abstract fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType)
    abstract suspend fun readFromConsole(): String
}