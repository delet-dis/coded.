package com.hits.coded.presentation.activities.editorActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hits.coded.data.repositoriesImplementations.ConsoleRepositoryImplementation
import com.hits.coded.databinding.ActivityEditorBinding
import com.hits.coded.domain.repositories.ConsoleRepository
import com.hits.coded.presentation.activities.onboardingActivity.viewModel.OnboardingActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding

    private val viewModel = EditorActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding = ActivityEditorBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.writeButton.setOnClickListener {
            Log.d("CONSOLE_DEBUG", "click!")
            viewModel.writeSmthToConsole()
        }
    }

}