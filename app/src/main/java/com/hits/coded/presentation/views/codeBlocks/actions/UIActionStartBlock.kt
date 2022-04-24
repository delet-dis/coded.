package com.hits.coded.presentation.views.codeBlocks.actions

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.databinding.ViewActionStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIActionStartBlock constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewActionStartBinding

    init {
        inflate(
            context,
            R.layout.view_action_start,
            this
        ).also { view ->
            binding = ViewActionStartBinding.bind(view)
        }
    }
}