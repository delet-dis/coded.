package com.hits.coded.presentation.activities.onboardingActivity.fragmentStateAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hits.coded.data.models.onboardingActivity.dataClasses.OnboardingScreen

class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val items: Array<OnboardingScreen>
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int =
        items.size

    override fun createFragment(position: Int): Fragment =
        items[position].screen
}