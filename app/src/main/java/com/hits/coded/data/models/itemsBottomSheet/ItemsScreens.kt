package com.hits.coded.data.models.itemsBottomSheet

import com.hits.coded.R
import com.hits.coded.presentation.activities.onboardingActivity.fragments.FirstOnboardingStepFragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.SecondOnboardingStepFragment
import com.hits.coded.presentation.activities.onboardingActivity.fragments.ThirdOnboardingStepFragment

enum class ItemsScreens(val itemsScreen: ItemsScreen) {
    VARIABLES(ItemsScreen(FirstOnboardingStepFragment(), R.string.variables)),
    LOGIC(ItemsScreen(SecondOnboardingStepFragment(), R.string.logic)),
    LOOPS(ItemsScreen(ThirdOnboardingStepFragment(), R.string.loops)),
    ACTIONS(ItemsScreen(ThirdOnboardingStepFragment(), R.string.actions)),
}