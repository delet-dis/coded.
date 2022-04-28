package com.hits.coded.data.repositoriesImplementations

import android.content.Context
import com.hits.coded.R
import com.hits.coded.data.models.interpreterException.dataClasses.InterpreterException
import com.hits.coded.domain.repositories.InterpreterCallerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InterpreterCallerRepositoryImplementation
@Inject constructor(@ApplicationContext private val context: Context) :
    InterpreterCallerRepository() {

    private val errorStrings =
        arrayOf(
            R.string.typeMismatchError,
            R.string.arrayOutOFBoundError,
            R.string.divisionByZeroError
        )

    private val _executionResult: MutableSharedFlow<InterpreterException?> =
        MutableSharedFlow(1, 0, BufferOverflow.DROP_OLDEST)
    override val executionResult: Flow<InterpreterException?>
        get() = _executionResult


    init {
        _executionResult.tryEmit(null)
    }

    override suspend fun callInterpreter() {
        try {
            TODO("call interpreter")
        }
        catch (error: InterpreterException) {
            error.msg = context.getString(errorStrings[error.errorCode.ordinal])
            _executionResult.emit(error)
        }
        finally {
            _executionResult.emit(null)
        }
    }

}