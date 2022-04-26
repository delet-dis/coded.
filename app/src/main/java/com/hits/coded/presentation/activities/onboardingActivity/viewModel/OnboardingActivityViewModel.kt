package com.hits.coded.presentation.activities.onboardingActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hits.coded.data.models.onboarding.dataClasses.OnboardingScreen
import com.hits.coded.data.models.onboarding.enums.OnboardingScreens
import com.hits.coded.data.models.sharedPreferences.useCases.SharedPreferencesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingActivityViewModel @Inject constructor(
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {
    private val _isAvailableToHideGreeting = MutableLiveData(false)
    val isAvailableToHideGreeting: LiveData<Boolean>
        get() = _isAvailableToHideGreeting

    private val _currentlyDisplayingOnboardingScreenNumber = MutableLiveData(0)
    val currentlyDisplayingOnboardingScreenNumber: LiveData<Int>
        get() = _currentlyDisplayingOnboardingScreenNumber

    var isOnboardingPassed =
        sharedPreferencesUseCases.checkIsOnboardingPassedUseCase.checkIsOnboardingPassed()

    fun initGreetingHideCountdown() {
        viewModelScope.launch {
            delay(1500)
            _isAvailableToHideGreeting.postValue(true)
        }
    }

    fun setCurrentlyDisplayingOnboardingScreenNumber(newValue: Int) {
        this._currentlyDisplayingOnboardingScreenNumber.postValue(newValue)
    }

    fun setOnboardingPassed() {
        sharedPreferencesUseCases.changeOnboardingPassedStateUseCase.changeOnboardingPassed(true)
        isOnboardingPassed =
            sharedPreferencesUseCases.checkIsOnboardingPassedUseCase.checkIsOnboardingPassed()
    }

    fun getOnboardingScreens(): Array<OnboardingScreen> {
        val onboardingScreens = ArrayList<OnboardingScreen>()

        OnboardingScreens.values().forEach {
            onboardingScreens.add(it.onboardingScreen)
        }

        return onboardingScreens.toTypedArray()
    }
}