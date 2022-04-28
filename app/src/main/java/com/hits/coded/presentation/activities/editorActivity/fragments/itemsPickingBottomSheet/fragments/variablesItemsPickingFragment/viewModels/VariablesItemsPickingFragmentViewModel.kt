package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments.variablesItemsPickingFragment.viewModels

import androidx.lifecycle.ViewModel
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VariablesItemsPickingFragmentViewModel @Inject constructor(private val heapUseCases: HeapUseCases) :
    ViewModel() {

}