package com.hits.coded.presentation.activities.editorActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorActivityViewModel @Inject constructor() : ViewModel() {
    private val _isBarsCollapsed = MutableLiveData(false)
    val isBarsCollapsed: LiveData<Boolean>
        get() = _isBarsCollapsed

    fun collapseBars() = _isBarsCollapsed.postValue(true)

    fun openBars() = _isBarsCollapsed.postValue(false)
}