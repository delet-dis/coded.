package com.hits.coded.data.models.onboarding

import com.hits.coded.presentation.activities.onboardingActivity.fragments.FirstOnboardingStepFragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.SecondOnboardingStepFragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.ThirdOnboardingStepFragment

enum class OnboardingScreens(val onboardingScreen: OnboardingScreen) {
    FIRST_SCREEN(OnboardingScreen(FirstOnboardingStepFragment())),
    SECOND_SCREEN(OnboardingScreen(SecondOnboardingStepFragment())),
    THIRD_SCREEN(OnboardingScreen(ThirdOnboardingStepFragment())),
}