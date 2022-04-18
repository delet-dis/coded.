package com.hits.coded.presentation.activities.onboardingActivity.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OnboardingActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

}