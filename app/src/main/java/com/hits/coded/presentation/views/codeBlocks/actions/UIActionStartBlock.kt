package com.hits.coded.presentation.views.codeBlocks.actions

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.data.models.uiCodeBLocks.UICodeBlockInterface
import com.hits.coded.databinding.ViewActionStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIActionStartBlock constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UICodeBlockInterface {
    private val binding: ViewActionStartBinding

    init {
        inflate(
            context,
            R.layout.view_action_start,
            this
        ).also { view ->
            binding = ViewActionStartBinding.bind(view)
        }

        this.initDragNDropGesture(this, DRAG_N_DROP_TAG)
    }

    private companion object {
        const val DRAG_N_DROP_TAG = "ACTION_START_BLOCK_"
    }
}