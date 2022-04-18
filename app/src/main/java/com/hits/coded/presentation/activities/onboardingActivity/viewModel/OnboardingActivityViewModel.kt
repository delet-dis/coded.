package com.hits.coded.presentation.activities.onboardingActivity.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _isAvailableToHideGreeting = MutableLiveData(false)
    val isAvailableToHideGreeting: LiveData<Boolean>
        get() = _isAvailableToHideGreeting

    fun initGreetingHideCountdown() {
        viewModelScope.launch {
            delay(1500)
            _isAvailableToHideGreeting.postValue(true)
        }
    }
}