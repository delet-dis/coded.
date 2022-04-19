package com.hits.coded.domain.useCases.sharedPreferences

import com.hits.coded.domain.repositories.SharedPreferencesRepository

class ChangeOnboardingPassedStateUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    fun changeOnboardingPassed(newValue: Boolean) =
        sharedPreferencesRepository.updateSharedPreferencesValueByKey(
            IS_ONBOARDING_PASSED,
            newValue
        )

    private companion object {
        const val IS_ONBOARDING_PASSED = "isOnboardingPassed"
    }
}