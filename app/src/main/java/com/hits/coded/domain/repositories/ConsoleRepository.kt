package com.hits.coded.domain.repositories

import android.graphics.Color
import android.text.SpannableString
import kotlinx.coroutines.flow.Flow


abstract class ConsoleRepository {
    abstract val buffer: Flow<ArrayDeque<SpannableString>>
    abstract val isInputAvailable: Flow<Boolean>
    abstract fun clear()
    abstract fun writeToConsole(input: String, color: Int = Color.WHITE)
    abstract suspend fun readFromConsole(): String
}