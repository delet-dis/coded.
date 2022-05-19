package com.hits.coded.presentation.activities.editorActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.console.useCases.ConsoleUseCases
import com.hits.coded.data.models.interpreterCaller.useCases.InterpreterCallerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorActivityViewModel @Inject constructor(
    private val interpreterCallerUseCases: InterpreterCallerUseCases,
    private val consoleUseCases: ConsoleUseCases,
) :
    ViewModel() {
    private val _isBarsCollapsed = MutableLiveData(false)
    val isBarsCollapsed: LiveData<Boolean>
        get() = _isBarsCollapsed

    private val _isCodeExecuting = MutableLiveData(false)
    val isCodeExecuting: LiveData<Boolean>
        get() = _isCodeExecuting

    val isConsoleInputAvailable =
        consoleUseCases.checkIsInputAvailableUseCase.checkIsInputAvailable().asLiveData()

    val codeExecutionResult =
        interpreterCallerUseCases.getExecutionResultsUseCase.getExecutionResult().asLiveData()

    private lateinit var processingJob: Job

    fun hideBars() = _isBarsCollapsed.postValue(true)

    fun toggleBars() = _isBarsCollapsed.value?.let {
        _isBarsCollapsed.postValue(!it)
    }

    fun executeCode(startBlock: StartBlock) {
        _isCodeExecuting.postValue(true)

        processingJob = viewModelScope.launch(Dispatchers.IO) {
            interpreterCallerUseCases.callInterpreterUseCase.callInterpreter(startBlock)
            stopCodeExecution()
        }
    }

    fun stopCodeExecution() {
//        TODO: interpreterCallerUseCases.stopInterpreterUseCase.stopInterpreter()

        viewModelScope.launch(Dispatchers.IO) {
            consoleUseCases.flushUseCase.flush()
        }

        _isCodeExecuting.postValue(false)
    }
}