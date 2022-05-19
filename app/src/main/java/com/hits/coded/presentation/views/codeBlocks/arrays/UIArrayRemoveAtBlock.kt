package com.hits.coded.presentation.views.codeBlocks.arrays

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.UIElementHandlesCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.UIElementSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ArrayBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ArrayBlockType
import com.hits.coded.databinding.ViewArrayRemoveAtBlockBinding

class UIArrayRemoveAtBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface,
    UIElementHandlesCustomRemoveViewProcessInterface, UIElementSavesNestedBlocksInterface,
    UICodeBlockSupportsErrorDisplaying {
    private val binding: ViewArrayRemoveAtBlockBinding

    override val nestedUIBlocks: ArrayList<View?> = ArrayList()

    private var _block = ArrayBlock(ArrayBlockType.REMOVE_AT)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    init {
        inflate(
            context,
            R.layout.view_array_remove_at_block,
            this
        ).also { view ->
            binding = ViewArrayRemoveAtBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()

        initValueToAddListener()

        initArrayNameChangeListener()
    }

    private fun initValueToAddListener() =
        binding.index.addTextChangedListener {
            _block.value = it.toString()
        }

    private fun initArrayNameChangeListener() =
        binding.arrayName.addTextChangedListener {
            _block.array = it.toString()
        }

    override fun initDragAndDropListener() {
        binding.arrayName.setOnDragListener { _, _ ->
            true
        }

        binding.index.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            (draggableItem as? UINestedableCodeBlock)?.let {
                val itemParent = draggableItem.parent as? ViewGroup

                itemParent?.let {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED,
                        DragEvent.ACTION_DRAG_LOCATION -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            scalePlusAnimation(binding.firstCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            scaleMinusAnimation(binding.firstCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(itemParent, draggableItem)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(draggableItem)

                            return@setOnDragListener true
                        }

                        else -> return@setOnDragListener false
                    }
                }
            }
            true
        }
    }

    private fun handleDropEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) = with(binding) {
        if (draggableItem != this@UIArrayRemoveAtBlock) {
            scaleMinusAnimation(binding.firstCard)

            itemParent.removeView(draggableItem)

            processViewWithCustomRemoveProcessRemoval(itemParent, draggableItem)

            index.apply {
                setText("")
                visibility = INVISIBLE
            }

            clearNestedBlocksFromParent(firstCard)

            nestedUIBlocks.add(draggableItem)
            firstCard.addView(draggableItem)

            (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                _block.value = it
            }
        }
    }

    private fun handleDragEndedEvent(
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        if ((draggableItem as? UICodeBlockWithDataInterface)?.block == _block.value) {
            draggableItem.x = 0f
            draggableItem.y = 0f
        }

        this.invalidate()
    }

    override fun customRemoveView(view: View) {
        nestedUIBlocks.remove(view)
        binding.secondCard.removeView(view)

        view.tag = null

        _block.value = null

        binding.index.apply {
            setText("")
            visibility = VISIBLE
        }
    }

    override fun displayError() =
        binding.backgroundImage.setImageResource(R.drawable.error_block)

    override fun hideError() =
        binding.backgroundImage.setImageResource(R.drawable.array_block)

    private companion object {
        const val DRAG_AND_DROP_TAG = "ARRAY_REMOVE_AT_BLOCK_"
    }
}