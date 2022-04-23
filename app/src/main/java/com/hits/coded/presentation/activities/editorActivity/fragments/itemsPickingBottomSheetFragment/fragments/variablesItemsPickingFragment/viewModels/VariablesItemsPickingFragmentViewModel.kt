package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VariablesItemsPickingFragmentViewModel @Inject constructor(private val heapUseCases: HeapUseCases) :
    ViewModel() {
    val variablesList = heapUseCases.getVariablesUseCase.getVariables().asLiveData()

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
}