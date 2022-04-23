package com.hits.coded.presentation.views.codeBlocks

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.uiCodeBLocks.ViewWithVariableCodeBlock
import com.hits.coded.databinding.ViewVariableBlockBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.ViewComponentManager
import dagger.hilt.android.qualifiers.ActivityContext

@AndroidEntryPoint
class UIVariableBlock @JvmOverloads constructor(
    @ActivityContext context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ViewWithVariableCodeBlock {
    private lateinit var binding: ViewVariableBlockBinding

    private lateinit var displayingBlock: VariableBlock

    init {
        inflate(
            (context as ViewComponentManager.FragmentContextWrapper).baseContext,
            R.layout.view_variable_block,
            this
        ).also { view ->
            binding = ViewVariableBlockBinding.bind(view)
        }
    }

    override fun setVariableBlock(block: VariableBlock) {
        displayingBlock = block

        displayVariableBlock()
    }

    private fun displayVariableBlock() {
        binding.variableName.text = displayingBlock.variableName
    }
}