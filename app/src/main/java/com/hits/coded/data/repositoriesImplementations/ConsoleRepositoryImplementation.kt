package com.hits.coded.data.repositoriesImplementations

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.hits.coded.data.models.console.enums.ConsoleMessageType
import com.hits.coded.domain.repositories.ConsoleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsoleRepositoryImplementation @Inject constructor(
    @ApplicationContext private val context: Context
) :
    ConsoleRepository() {

    private val _bufferValue: ArrayDeque<SpannableString> = ArrayDeque(BUFFER_SIZE)

    private var _buffer: MutableSharedFlow<ArrayDeque<SpannableString>> =
        MutableSharedFlow(1, 0, BufferOverflow.DROP_OLDEST)
    override val buffer: Flow<ArrayDeque<SpannableString>>
        get() = _buffer

    private var _isInputAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isInputAvailable: Flow<Boolean>
        get() = _isInputAvailable


    init {
        _buffer.tryEmit(_bufferValue)
    }

    override fun clear() {
        _bufferValue.clear()
        _buffer.tryEmit(_bufferValue)
    }

    override suspend fun readFromConsole(): String {
        _isInputAvailable.emit(true)
        val input = buffer.drop(1).first().first()
        _isInputAvailable.emit(false)
        return input.toString()
    }

    override fun writeToConsole(input: String, consoleMessageType: ConsoleMessageType) {
        if (_bufferValue.size == BUFFER_SIZE) {
            _bufferValue.removeLast()
        }

        _bufferValue.addFirst(SpannableString(input).apply {
            setSpan(
                ForegroundColorSpan(context.getColor(consoleMessageType.colorResourceId)),
                0,
                input.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        })
        _buffer.tryEmit(_bufferValue)
    }


    private companion object {
        const val BUFFER_SIZE = 100
    }
}