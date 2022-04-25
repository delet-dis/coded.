package com.hits.coded.data.repositoriesImplementations

import com.hits.coded.domain.repositories.ConsoleRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsoleRepositoryImplementation @Inject constructor() : ConsoleRepository() {

    private val _bufferValue: ArrayDeque<String> = ArrayDeque(100)

    private var _buffer: MutableSharedFlow<ArrayDeque<String>> =
        MutableSharedFlow(1, 0, BufferOverflow.DROP_OLDEST)
    override val buffer: Flow<ArrayDeque<String>>
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
        return input
    }

    override fun writeToConsole(input: String) {
        if (_bufferValue.size == BUFFER_SIZE) {
            _bufferValue.removeLast()
        }
        _bufferValue.addFirst(input + "\n")
        _buffer.tryEmit(_bufferValue)
    }


    private companion object {
        const val BUFFER_SIZE = 100
    }
}