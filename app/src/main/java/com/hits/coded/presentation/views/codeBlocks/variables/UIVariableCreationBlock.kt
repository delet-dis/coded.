package com.hits.coded.presentation.views.codeBlocks.variables

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.databinding.ViewVariableCreateBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIVariableCreationBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewVariableCreateBlockBinding

    init {
        inflate(
            context,
            R.layout.view_variable_create_block,
            this
        ).also { view ->
            binding = ViewVariableCreateBlockBinding.bind(view)
        }
    }
}