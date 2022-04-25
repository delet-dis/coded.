package com.hits.coded.presentation.views.codeField

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.hits.coded.R
import com.hits.coded.databinding.ViewCodeFieldBinding
import com.hits.coded.presentation.views.codeBlocks.actions.UIActionStartBlock
import com.hits.coded.presentation.views.codeBlocks.variables.UIVariableCreationBlock
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

        addBlock(UIVariableCreationBlock(context))
    }

    fun addBlock(view: View) {
        view.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        binding.fieldLayout.addView(view)

        view.updateLayoutParams<LayoutParams> {
            topToTop = R.id.fieldLayout
            bottomToBottom = R.id.fieldLayout
            leftToLeft = R.id.fieldLayout
            rightToRight = R.id.fieldLayout
        }
    }
}