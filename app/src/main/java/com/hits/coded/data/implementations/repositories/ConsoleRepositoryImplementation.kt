package com.hits.coded.data.implementations.repositories

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.hits.coded.data.models.console.enums.ConsoleMessageType
import com.hits.coded.domain.repositories.ConsoleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
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

    override val output =
        MutableSharedFlow<SpannableStringBuilder>(1, 0, BufferOverflow.DROP_OLDEST)
    private var outputBuffer = ArrayDeque<Pair<String, ConsoleMessageType>>(BUFFER_SIZE)
    private val inputBuffer =
        MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
    override val isInputAvailable  = MutableStateFlow(false)

    private var mutex = Mutex()


    init {
        output.tryEmit(SpannableStringBuilder(""))
    }

    override fun clear() {
        outputBuffer.clear()
        output.tryEmit(SpannableStringBuilder(""))
    }

    override suspend fun readFromConsole(): String {
        flush()
        isInputAvailable.emit(true)
        val input = inputBuffer.first()
        isInputAvailable.emit(false)
        return input
    }

    override suspend fun flush() {
        mutex.withLock {
            val resultString = SpannableStringBuilder()
            for (str in outputBuffer) {
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

            output.emit(resultString)
        }
    }

    override fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType) {
        if(!mutex.tryLock())
            return // flushing now

        if (outputBuffer.size == BUFFER_SIZE) {
            outputBuffer.removeFirst()
        }

        outputBuffer.add(Pair(input + '\n', consoleMessageType))
        inputBuffer.tryEmit(input)

        mutex.unlock()
    }


    private companion object {
        const val BUFFER_SIZE = 200
    }
}