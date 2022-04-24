package com.hits.coded.presentation.views.codeField

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.databinding.ViewCodeFieldBinding
import com.hits.coded.presentation.views.codeBlocks.actions.UIActionStartBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeField constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewCodeFieldBinding

    private lateinit var startBlock: UIActionStartBlock

    init {
        inflate(
            context,
            R.layout.view_code_field,
            this
        ).also { view ->
            binding = ViewCodeFieldBinding.bind(view)
        }

        initStartBlock()
    }

    private fun initStartBlock() {
        startBlock = UIActionStartBlock(context)

        addBlock(startBlock)
    }

    fun addBlock(view: View) {
        val addingViewLayoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addingViewLayoutParams.apply {
            bottomToBottom = binding.fieldLayout.id
            startToStart = binding.fieldLayout.id
            endToEnd = binding.fieldLayout.id
            leftToLeft = binding.fieldLayout.id
        }

        view.layoutParams = addingViewLayoutParams

        binding.fieldLayout.addView(view)
    }
}