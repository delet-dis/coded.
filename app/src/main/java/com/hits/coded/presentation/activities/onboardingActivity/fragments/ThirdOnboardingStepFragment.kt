package com.hits.coded.presentation.activities.onboardingActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.databinding.FragmentFirstOnboardingStepBinding
import com.hits.coded.databinding.FragmentSecondOnboardingStepBinding
import com.hits.coded.databinding.FragmentThirdOnboardingStepBinding

class ThirdOnboardingStepFragment: Fragment() {
    private lateinit var binding: FragmentThirdOnboardingStepBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentThirdOnboardingStepBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

        }
    }
}