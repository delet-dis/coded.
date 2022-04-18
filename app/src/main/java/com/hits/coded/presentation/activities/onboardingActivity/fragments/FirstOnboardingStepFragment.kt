package com.hits.coded.presentation.activities.onboardingActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.databinding.FragmentFirstOnboardingStepBinding

class FirstOnboardingStepFragment: Fragment() {
    private lateinit var binding: FragmentFirstOnboardingStepBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentFirstOnboardingStepBinding.inflate(layoutInflater)

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