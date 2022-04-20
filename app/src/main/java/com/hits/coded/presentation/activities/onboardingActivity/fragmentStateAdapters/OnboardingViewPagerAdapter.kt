package com.hits.coded.presentation.activities.onboardingActivity.fragmentStateAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hits.coded.data.models.onboarding.OnboardingScreens

class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int =
        OnboardingScreens.values().size

    override fun createFragment(position: Int): Fragment =
        OnboardingScreens.values()[position].screen
}