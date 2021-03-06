package com.hits.coded.presentation.activities.onboardingActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.databinding.FragmentSecondOnboardingStepBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondOnboardingStepFragment : Fragment() {
    private lateinit var binding: FragmentSecondOnboardingStepBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentSecondOnboardingStepBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }
}