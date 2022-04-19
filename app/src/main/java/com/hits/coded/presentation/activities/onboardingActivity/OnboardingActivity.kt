package com.hits.coded.presentation.activities.onboardingActivity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager2.widget.ViewPager2
import com.hits.coded.R
import com.hits.coded.databinding.ActivityOnboardingBinding
import com.hits.coded.presentation.activities.editorActivity.EditorActivity
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

        checkIsOnboardingPassed()

        initViewPager()

        initViewPagerListener()

        initNavigationToOnboarding()

        initViewPagerCurrentPageObserver()

        initRadioDots()

        initNextButtonWrapperOnClick()
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
        binding.viewPager.adapter = OnboardingViewPagerAdapter(this@OnboardingActivity)
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

    private fun initRadioDots() {
        binding.viewPager.adapter?.let {
            for (i in 1..it.itemCount) {
                val radioButton = RadioButton(this).apply {
                    text = ""
                    buttonDrawable =
                        AppCompatResources.getDrawable(
                            this@OnboardingActivity,
                            R.drawable.onboarding_radio_button_style
                        )
                    isClickable = false

                    setPadding(
                        0, 0,
                        resources.getDimension(R.dimen.onboardingButtonsSpacing).toInt(),
                        0
                    )
                }

                binding.radioGroup.addView(radioButton)
            }
        }
    }

    private fun initViewPagerListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                viewModel.setCurrentlyDisplayingOnboardingScreenNumber(position)
            }
        })
    }

    private fun initViewPagerCurrentPageObserver() {
        viewModel.currentlyDisplayingOnboardingScreenNumber.observe(this) {
            with(binding.radioGroup) {
                check(getChildAt(it).id)
            }

            val buttonTextView = (binding.nextButtonWrapper.getChildAt(1) as? TextView)

            binding.viewPager.adapter?.let { adapter ->
                if (it == adapter.itemCount - 1) {
                    buttonTextView?.text = getString(R.string.imReady)
                } else {
                    buttonTextView?.text = getString(R.string.whatSMore)
                }
            }
        }
    }

    private fun initNextButtonWrapperOnClick() {
        with(binding) {
            nextButtonWrapper.setOnClickListener {
                viewPager.adapter?.let { adapter ->
                    if (viewPager.currentItem < adapter.itemCount - 1) {
                        viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                    } else {
                        viewModel.setOnboardingPassed()
                        checkIsOnboardingPassed()
                    }
                }
            }
        }
    }

    private fun checkIsOnboardingPassed() {
        if (viewModel.isOnboardingPassed) {
            startActivity(Intent(this, EditorActivity::class.java))
            finish()
        }
    }
}