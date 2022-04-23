package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VariablesItemsPickingFragmentViewModel @Inject constructor(private val heapUseCases: HeapUseCases) :
    ViewModel() {
    private var _variablesList = MutableLiveData<Array<VariableBlock>>()
    val variablesList: LiveData<Array<VariableBlock>>
        get() = _variablesList

    private val _isAvailableToObserveVariables = MutableLiveData(false)
    val isAvailableToObserveVariables: LiveData<Boolean>
        get() = _isAvailableToObserveVariables

    init {
        initVariablesListObserving()
    }

    fun addVariable(variableName: String) =
        viewModelScope.launch(Dispatchers.IO) {
            heapUseCases.addVariableUseCase.addVariable(
                variableName
            )
        }

    fun deleteVariable(variableName: String) =
        viewModelScope.launch(Dispatchers.IO) {
            heapUseCases.deleteVariableUseCase.deleteVariable(
                variableName
            )
        }

    private fun initVariablesListObserving() {
        viewModelScope.launch(Dispatchers.IO) {
//            heapUseCases.getVariablesUseCase.getVariables().collect {
//                _variablesList.postValue(it)
//            }

            heapUseCases.getVariablesUseCase.heap.collect {
                val arrayToPost = ArrayList<VariableBlock>()

                it.forEach { entry ->
                    VariableBlock(
                        VariableBlockType.VARIABLE_GET,
                        entry.key,
                        null,
                        null,
                        null
                    )
                }

                _variablesList.postValue(arrayToPost.toTypedArray())
            }
        }
    }
}