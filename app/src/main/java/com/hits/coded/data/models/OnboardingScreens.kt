package com.hits.coded.data.models

import androidx.fragment.app.Fragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.FirstOnboardingStepFragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.SecondOnboardingStepFragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.ThirdOnboardingStepFragment

enum class OnboardingScreens(val screen: Fragment) {
    FIRST_SCREEN(FirstOnboardingStepFragment()),
    SECOND_SCREEN(SecondOnboardingStepFragment()),
    THIRD_SCREEN(ThirdOnboardingStepFragment()),
}