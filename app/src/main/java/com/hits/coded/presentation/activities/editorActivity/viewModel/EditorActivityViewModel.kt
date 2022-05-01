package com.hits.coded.presentation.activities.editorActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.interpreterCaller.useCases.InterpreterCallerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorActivityViewModel @Inject constructor(
    private val interpreterCallerUseCases: InterpreterCallerUseCases
) :
    ViewModel() {
    private val _isBarsCollapsed = MutableLiveData(false)
    val isBarsCollapsed: LiveData<Boolean>
        get() = _isBarsCollapsed

    private val _isCodeExecuting = MutableLiveData(false)
    val isCodeExecuting: LiveData<Boolean>
        get() = _isCodeExecuting

    fun hideBars() = _isBarsCollapsed.postValue(true)

    fun toggleBars() = _isBarsCollapsed.value?.let {
        _isBarsCollapsed.postValue(!it)
    }

    fun executeCode(startBlock: StartBlock) {
        _isCodeExecuting.postValue(true)
        viewModelScope.launch {
            interpreterCallerUseCases.callInterpreterUseCase.callInterpreter(startBlock)

            _isCodeExecuting.postValue(false)
        }
    }
}