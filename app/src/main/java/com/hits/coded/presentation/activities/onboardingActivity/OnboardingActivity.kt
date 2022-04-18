package com.hits.coded.presentation.activities.onboardingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.hits.coded.databinding.ActivityOnboardingBinding
import com.hits.coded.presentation.activities.onboardingActivity.viewModel.OnboardingActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private val viewModel: OnboardingActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    private fun initBinding(){
        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}