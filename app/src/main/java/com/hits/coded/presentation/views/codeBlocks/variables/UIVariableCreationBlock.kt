package com.hits.coded.presentation.views.codeBlocks.variables

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.databinding.ViewVariableCreateBlockBinding
import com.hits.coded.presentation.views.codeBlocks.shadowBuilder.BlockDragShadowBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

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

        initDragNDropGesture()
    }

    private fun initDragNDropGesture() {
        tag = DRAG_N_DROP_TAG + Random.Default.nextInt().toString()

        this.setOnLongClickListener {
            val item = ClipData.Item(tag as? CharSequence)

            val dataToDrag = ClipData(
                tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val maskShadow = BlockDragShadowBuilder(this)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                startDrag(dataToDrag, maskShadow, this, 0)
            } else {
                startDragAndDrop(dataToDrag, maskShadow, this, 0)
            }

            visibility = View.INVISIBLE

            true
        }
    }

    private companion object {
        const val DRAG_N_DROP_TAG = "VARIABLE_CREATION_BLOCK_"
    }
}