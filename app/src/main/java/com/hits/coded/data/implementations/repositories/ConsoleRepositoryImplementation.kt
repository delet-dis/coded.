package com.hits.coded.data.implementations.repositories

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.hits.coded.data.models.console.enums.ConsoleMessageType
import com.hits.coded.domain.repositories.ConsoleRepository
import console.Console
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsoleRepositoryImplementation @Inject constructor(
    @ApplicationContext private val context: Context
) :
    ConsoleRepository() {

    private var mutex = Mutex()
    private var bufferValue = ArrayDeque<Pair<String, ConsoleMessageType>>(BUFFER_SIZE)
    private var buffer: MutableSharedFlow<ArrayDeque<Pair<String, ConsoleMessageType>>> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

    private var _isInputAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isInputAvailable: Flow<Boolean>
        get() = _isInputAvailable


    init {
    }

    override fun clear() {
        Console.write(SpannableStringBuilder(""))
    }

    override suspend fun readFromConsole(): String {
        flush()
        _isInputAvailable.emit(true)
        val input = buffer.first().first().first
        _isInputAvailable.emit(false)
        return input
    }

    override suspend fun flush() {
        mutex.withLock {
            val resultString = SpannableStringBuilder()
            for (str in bufferValue) {
                resultString.append(
                    SpannableString(str.first).apply {
                        setSpan(
                            ForegroundColorSpan(context.getColor(str.second.colorResourceId)),
                            0,
                            str.first.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                )
            }

            Console.write(resultString)
        }
    }

    override fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType) {
        if(!mutex.tryLock())
            return // flushing now

        if (bufferValue.size == BUFFER_SIZE) {
            bufferValue.removeFirst()
        }

        bufferValue.add(Pair(input + '\n', consoleMessageType))
        buffer.tryEmit(bufferValue)

        mutex.unlock()
    }


    private companion object {
        const val BUFFER_SIZE = 200
    }
}