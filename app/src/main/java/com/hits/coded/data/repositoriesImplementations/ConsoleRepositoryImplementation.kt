package com.hits.coded.data.repositoriesImplementations

import android.util.Log
import com.hits.coded.domain.repositories.ConsoleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsoleRepositoryImplementation @Inject constructor() : ConsoleRepository() {
    private var _buffer: MutableStateFlow<ArrayDeque<String>> = MutableStateFlow(ArrayDeque(100))
    override val buffer: Flow<ArrayDeque<String>>
        get() = _buffer

    private var _isInputAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isInputAvailable: Flow<Boolean>
        get() = _isInputAvailable


    override suspend fun clear() {
        _buffer.value.clear()
        _buffer.emit(_buffer.value)
    }

    override suspend fun readFromConsole(): String {
        _isInputAvailable.emit(true)
        val input = buffer.single().first()
        _isInputAvailable.emit(false)
        return input
    }

    override suspend fun writeToConsole(input: String) {
        if (_buffer.value.size == BUFFER_SIZE) {
            _buffer.value.removeLast()
        }
        _buffer.value.addFirst(input + "\n")

        Log.d("CONSOLE_DEBUG", _buffer.value.toString())

        _buffer.emit(_buffer.value)
    }

    private companion object {
        const val BUFFER_SIZE = 100
    }
}