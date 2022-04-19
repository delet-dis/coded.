package com.hits.coded.domain.useCases.sharedPreferences

import com.hits.coded.domain.repositories.SharedPreferencesRepository

class CheckIsOnboardingPassedUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    fun checkIsOnboardingPassed() =
        sharedPreferencesRepository.getBooleanValueByKey(
            IS_ONBOARDING_PASSED
        )

    private companion object {
        const val IS_ONBOARDING_PASSED = "isOnboardingPassed"
    }
}