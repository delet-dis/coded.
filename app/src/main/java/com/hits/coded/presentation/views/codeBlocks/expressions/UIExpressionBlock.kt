package com.hits.coded.presentation.views.codeBlocks.expressions

import android.animation.AnimatorSet
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
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.databinding.ViewExpressionBlockBinding

class UIExpressionBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface,
    UIElementHandlesCustomRemoveViewProcessInterface, UICodeBlockSavesNestedBlocksInterface,
    UINestedableCodeBlock, UICodeBlockSupportsErrorDisplaying {
    private val binding: ViewExpressionBlockBinding

    override val nestedUIBlocks: ArrayList<View> = ArrayList()

    private var leftSide: Any? = Any()
        set(value) {
            field = value

            _block.leftSide = value
        }
    private var rightSide: Any? = Any()
        set(value) {
            field = value

            _block.rightSide = value
        }

    private var _block = ExpressionBlock()
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0
    override val animationSet: AnimatorSet = AnimatorSet()

    var blockType: ExpressionBlockType? = null
        set(value) {
            field = value

            value?.let {
                changeBlockType(it)
            }
        }

    init {
        inflate(
            context,
            R.layout.view_expression_block,
            this
        ).also { view ->
            binding = ViewExpressionBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()

        initCardsTextsListeners()
    }

    private fun changeBlockType(blockType: ExpressionBlockType) {
        _block.expressionBlockType = blockType

        binding.centerText.setText(blockType.resourceId)
    }

    private fun initCardsTextsListeners() {
        binding.leftCardText.addTextChangedListener {
            leftSide = it.toString()
        }

        binding.rightCardText.addTextChangedListener {
            rightSide = it.toString()
        }
    }

    override fun initDragAndDropListener() {
        binding.leftCardText.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            (draggableItem as? UINestedableCodeBlock)?.let {
                val itemParent = draggableItem.parent as? ViewGroup

                itemParent?.let {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED,
                        DragEvent.ACTION_DRAG_LOCATION -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            scalePlusAnimation(binding.leftCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            scaleMinusAnimation(binding.leftCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(binding.leftCard, itemParent, draggableItem)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(draggableItem)

                            return@setOnDragListener true
                        }

                        else -> return@setOnDragListener true
                    }
                }
            }
            false
        }

        binding.rightCardText.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            (draggableItem as? UINestedableCodeBlock)?.let {
                val itemParent = draggableItem.parent as? ViewGroup

                itemParent?.let {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED,
                        DragEvent.ACTION_DRAG_LOCATION -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            scalePlusAnimation(binding.rightCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            scaleMinusAnimation(binding.rightCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(binding.rightCard, itemParent, draggableItem)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(draggableItem)

                            return@setOnDragListener true
                        }

                        else -> return@setOnDragListener true
                    }
                }
            }
            false
        }
    }

    private fun handleDropEvent(
        parentCard: ViewGroup,
        itemParent: ViewGroup,
        draggableItem: View
    ) = with(binding) {
        if (draggableItem != this@UIExpressionBlock) {
            scaleMinusAnimation(parentCard)

            itemParent.removeView(draggableItem)

            processViewWithCustomRemoveProcessRemoval(itemParent, draggableItem)

            val draggableBlockWithData = draggableItem as? UICodeBlockWithDataInterface

            if (parentCard == leftCard) {
                leftCardText.apply {
                    setText("")
                    visibility = INVISIBLE
                }

                draggableBlockWithData?.block?.let {
                    leftSide = it
                }
            }

            if (parentCard == rightCard) {
                rightCardText.apply {
                    setText("")
                    visibility = INVISIBLE
                }

                draggableBlockWithData?.block?.let {
                    rightSide = it
                }
            }

            clearNestedBlocksFromParent(parentCard)

            nestedUIBlocks.add(draggableItem)
            parentCard.addView(draggableItem)
        }
    }

    private fun handleDragEndedEvent(
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        val draggableItemBlock = (draggableItem as? UICodeBlockWithDataInterface)?.block

        if (draggableItemBlock == _block.leftSide || draggableItemBlock == _block.rightSide) {
            draggableItem.x = 0f
            draggableItem.y = 0f
        }

        this.invalidate()
    }

    override fun customRemoveView(view: View) {
        nestedUIBlocks.remove(view)

        val removingViewBlock = (view as? UICodeBlockWithDataInterface)?.block

        if ((leftSide as? BlockBase) == removingViewBlock) {
            binding.leftCard.removeView(view)

            leftSide = null

            with(binding.leftCardText) {
                setText("")
                visibility = VISIBLE
            }
        }

        if ((rightSide as? BlockBase) == removingViewBlock) {
            binding.rightCard.removeView(view)

            leftSide = null

            with(binding.rightCardText) {
                setText("")
                visibility = VISIBLE
            }
        }
    }

    override fun displayError() =
        binding.backgroundImage.setImageResource(R.drawable.expression_block)

    override fun hideError() =
        binding.backgroundImage.setImageResource(R.drawable.error_small_block)

    private companion object {
        const val DRAG_AND_DROP_TAG = "EXPRESSION_BLOCK_"
    }
}