package com.hits.coded.presentation.views.console

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import com.hits.coded.R
import com.hits.coded.databinding.ViewConsoleBinding
import com.hits.coded.domain.extensions.hideKeyboard
import com.hits.coded.domain.extensions.showKeyboard
import com.hits.coded.presentation.views.console.viewModel.ConsoleViewModel
import console.Console
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Console @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewConsoleBinding

    @Inject
    lateinit var viewModel: ConsoleViewModel

    init {
        inflate(
            context,
            R.layout.view_console,
            this
        ).also { view ->
            binding = ViewConsoleBinding.bind(view)
        }

        initConsoleObserving()

        initIsInputAvailableObserving()

        initConsoleSubmitting()
    }


    private fun initConsoleObserving() =
    viewModel.consoleBuffer.observe(context as LifecycleOwner) {
        Console.write(it)
    }


    private fun initIsInputAvailableObserving() =
        viewModel.isAvailableToInput.observe(context as LifecycleOwner) {
            binding.enteredText.isEnabled = it

            if (!it) {
                binding.enteredText.clearFocus()

                binding.enteredText.hideKeyboard()
            } else {
                binding.enteredText.requestFocus()

                binding.enteredText.showKeyboard()
            }
        }

    private fun initConsoleSubmitting() {
        with(binding.enteredText) {
            addTextChangedListener {
                val enteredText = it.toString()

                if (enteredText.contains("\n")) {
                    viewModel.submitStringToConsole(enteredText.replace("\n", ""))

                    setText("")

                    hideKeyboard()
                }
            }
        }
    }
}