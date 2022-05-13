package com.hits.coded.data.implementations.repositories

import android.content.Context
import android.text.SpannableString
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsoleRepositoryImplementation @Inject constructor(
    @ApplicationContext private val context: Context
) :
    ConsoleRepository() {
    private var textBuffer = ""
    private var markupBuffer = ArrayList<Pair<Int, ConsoleMessageType>>(BUFFER_SIZE)

    private var readBuffer: MutableSharedFlow<String> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

    private var _isInputAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isInputAvailable: Flow<Boolean>
        get() = _isInputAvailable


    init {
    }

    override fun clear() {
        Console.clear()
    }

    override suspend fun readFromConsole(): String {
        _isInputAvailable.emit(true)
        val input = readBuffer.first()
        _isInputAvailable.emit(false)
        return input
    }

    override fun flush() {
        val resultString = SpannableString(textBuffer)
        var strBeginning = 0

        for (markupPair in markupBuffer) {
            resultString.apply {
                setSpan(
                    ForegroundColorSpan(context.getColor(markupPair.second.colorResourceId)),
                    strBeginning,
                    strBeginning + markupPair.first,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            strBeginning += markupPair.first
        }

        textBuffer = ""
        markupBuffer.clear()

        Console.write(resultString)
    }

    override fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType) {

        readBuffer.tryEmit(input)

        textBuffer += input + '\n'

        val markupPair = Pair(input.length + 1, consoleMessageType)
        markupBuffer.add(markupPair)

        if (markupBuffer.size >= BUFFER_SIZE) {
            flush()
        }
    }


    private companion object {
        const val BUFFER_SIZE = 1000
    }
}