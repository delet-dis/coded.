package com.hits.coded.presentation.activities.editorActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hits.coded.databinding.ActivityEditorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding

    private val viewModel: EditorActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        binding.writeButton.setOnClickListener {
            Log.d("CONSOLE_DEBUG", "click!")
            viewModel.writeSmthToConsole()
        }
    }

    private fun initBinding() {
        binding = ActivityEditorBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

}