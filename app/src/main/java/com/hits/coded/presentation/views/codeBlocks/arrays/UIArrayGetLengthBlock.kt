package com.hits.coded.presentation.views.codeBlocks.arrays

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ArrayBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType
import com.hits.coded.databinding.ViewArrayLengthBlockBinding

class UIArrayGetLengthBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UICodeBlockElementHandlesDragAndDropInterface, UIElementHandlesDragAndDropInterface,
    UICodeBlockSupportsErrorDisplaying, UINestedableCodeBlock {
    private val binding: ViewArrayLengthBlockBinding

    private var _block = ArrayBlock(ArrayBlockType.GET_SIZE)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    init {
        inflate(
            context,
            R.layout.view_array_length_block,
            this
        ).also { view ->
            binding = ViewArrayLengthBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()

        initArrayNameChangeListener()
    }

    private fun initArrayNameChangeListener() =
        binding.arrayName.addTextChangedListener {
            _block.array = it.toString()
        }

    override fun initDragAndDropListener() {
        binding.arrayName.setOnDragListener { _, _ ->
            true
        }
    }

    override fun displayError() =
        binding.backgroundImage.setImageResource(R.drawable.error_small_block)

    override fun hideError() =
        binding.backgroundImage.setImageResource(R.drawable.array_small_block)

    private companion object {
        const val DRAG_AND_DROP_TAG = "ARRAY_GET_LENGTH_BLOCK_"
    }
}