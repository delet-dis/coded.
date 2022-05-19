package com.hits.coded.data.implementations.repositories

import android.content.Context
import com.hits.coded.R
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.data.models.interpreter.useCases.InterpreterUseCases
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.data.models.interpreter.useCases.helpers.StopInterpreterUseCases
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
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val interpreterUseCases: InterpreterUseCases,
    private val interpreterHelperUseCases: InterpreterHelperUseCases,
    private val consoleUseCases: ConsoleUseCases,
    private val heapUseCases: HeapUseCases,
    private val stopInterpreterUseCases: StopInterpreterUseCases
) : InterpreterCallerRepository() {

    private val errorStrings =
        arrayOf(
            R.string.typeMismatchError,
            R.string.arrayOutOFBoundError,
            R.string.divisionByZeroError,
            R.string.wrongStartPositionError,
            R.string.lackOfArgumentsError,
            R.string.variableNotExistError,
            R.string.wrongOperandError,
            R.string.invalidStringError,
            R.string.invalidBlockError,
            R.string.internalInterpreterError
        )

    private val _executionResult: MutableSharedFlow<InterpreterException?> =
        MutableSharedFlow(1, 0, BufferOverflow.DROP_OLDEST)
    override val executionResult: Flow<InterpreterException?>
        get() = _executionResult

    init {
        _executionResult.tryEmit(null)
    }

    override suspend fun callInterpreter(start: StartBlock) {
        heapUseCases.clearUseCase.clear()
        stopInterpreterUseCases.startInterpreterUseCase.startInterpreter()
        _executionResult.emit(null)
        try {
            interpreterUseCases.interpretStartBlock.interpretStartBlock(start)
        } catch (error: InterpreterException) {
            with(error) {
                blockID =
                    interpreterHelperUseCases.getCurrentIdVariableUseCase.getCurrentIdVariable()
                msg = context.getString(errorStrings[errorCode.ordinal])
                consoleUseCases.writeToConsoleUseCase.writeErrorToConsole(msg)
                _executionResult.emit(this)
            }
        }

        consoleUseCases.flushUseCase.flush()
    }

}