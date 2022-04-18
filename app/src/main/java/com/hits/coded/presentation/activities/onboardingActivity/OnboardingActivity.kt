package com.hits.coded.presentation.activities.onboardingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.hits.coded.R
import com.hits.coded.databinding.ActivityOnboardingBinding
import com.hits.coded.presentation.activities.onboardingActivity.fragmentStateAdapters.OnboardingViewPagerAdapter
import com.hits.coded.presentation.activities.onboardingActivity.viewModel.OnboardingActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private val viewModel: OnboardingActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initViewPager()

        initNavigationToOnboarding()
    }

    override fun onBackPressed() {
        with(binding.viewPager) {
            if (currentItem == 0) {
                super.onBackPressed()
            } else {
                currentItem -= 1
            }
        }
    }

    private fun initBinding() {
        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun initViewPager() {
        binding.viewPager.apply {
            adapter = OnboardingViewPagerAdapter(this@OnboardingActivity)
        }
    }

    private fun initNavigationToOnboarding() {
        with(viewModel) {
            initGreetingHideCountdown()

            isAvailableToHideGreeting.observe(this@OnboardingActivity) {
                if (it) {
                    binding.rootLayout.transitionToState(R.id.onboardingHidingGreeting)
                }
            }
        }
    }
}