package com.hits.coded.presentation.views.codeBlocks.actions

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.databinding.ViewActionStartBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UIActionStartBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UIElementHandlesDragAndDropInterface, UICodeBlockWithDataInterface,
    UICodeBlockWithLastTouchInformation, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockSavesNestedBlocksInterface {
    private val binding: ViewActionStartBinding

    private val nestedBlocksAsBlockBase = ArrayList<BlockBase>()

    override val nestedUIBlocks: ArrayList<View> = ArrayList()

    private var _block = StartBlock()
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    override val animationSet = AnimatorSet()

    init {
        inflate(
            context,
            R.layout.view_action_start,
            this
        ).also { view ->
            binding = ViewActionStartBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()
    }

    override fun initDragAndDropListener() =
        binding.parentConstraint.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as ViewGroup

            with(binding) {
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_STARTED,
                    DragEvent.ACTION_DRAG_LOCATION -> true

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        scalePlusAnimation(binding.parentConstraint)

                        true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        scaleMinusAnimation(parentConstraint)

                        true
                    }

                    DragEvent.ACTION_DROP -> {
                        handleDropEvent(itemParent, draggableItem)

                        true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        handleDragEndedEvent(itemParent, draggableItem)

                        true
                    }

                    else -> false
                }
            }
        }

    private fun handleDropEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) = with(binding) {
        if (draggableItem != this@UIActionStartBlock) {
            scaleMinusAnimation(parentConstraint)

            itemParent.removeView(draggableItem)

            nestedUIBlocks.add(draggableItem)
            nestedBlocksLayout.addView(draggableItem)

            (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                nestedBlocksAsBlockBase.add(it)

                _block.nestedBlocks = nestedBlocksAsBlockBase.toTypedArray()
            }
        }
    }

    private fun handleDragEndedEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        this@UIActionStartBlock.invalidate()

        with(binding.nestedBlocksLayout) {
            if (itemParent == this) {
                draggableItem.x = 0f

                if (childCount == 0) {
                    draggableItem.y = 0f
                } else {
                    val childRect = Rect()
                    getChildAt(childCount - 1)
                        .getDrawingRect(childRect)

                    offsetDescendantRectToMyCoords(
                        getChildAt(
                            childCount - 1
                        ), childRect
                    )

                    draggableItem.y = childRect.top.toFloat()
                }
            }
        }
    }

    override fun removeView(view: View?) {
        super.removeView(view)

        (view as? UICodeBlockWithDataInterface)?.block?.let {
            nestedBlocksAsBlockBase.remove(it)

            nestedUIBlocks.remove(view)

            _block.nestedBlocks = nestedBlocksAsBlockBase.toTypedArray()
        }
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "ACTION_START_BLOCK_"
    }
}