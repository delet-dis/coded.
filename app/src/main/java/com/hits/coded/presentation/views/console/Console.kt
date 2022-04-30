package com.hits.coded.presentation.views.console

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.hits.coded.R
import com.hits.coded.databinding.ViewConsoleBinding
import com.hits.coded.presentation.views.console.viewModel.ConsoleViewModel
import com.jraska.console.Console
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Console constructor(
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
    }

    private fun initConsoleObserving() =
        viewModel.consoleBuffer.observe(context as LifecycleOwner) {
            SpannableString

            Console.clear()

            Console.writeLine()
        }
}