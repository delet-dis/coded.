package com.hits.coded.data.models.sharedPreferences

import com.hits.coded.domain.useCases.sharedPreferences.ChangeOnboardingPassedStateUseCase
import com.hits.coded.domain.useCases.sharedPreferences.CheckIsOnboardingPassedUseCase

data class SharedPreferencesUseCases(
    val changeOnboardingPassedStateUseCase: ChangeOnboardingPassedStateUseCase,
    val checkIsOnboardingPassedUseCase: CheckIsOnboardingPassedUseCase,
)