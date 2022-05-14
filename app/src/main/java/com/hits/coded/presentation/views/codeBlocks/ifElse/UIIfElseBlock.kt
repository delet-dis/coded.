package com.hits.coded.presentation.views.codeBlocks.ifElse

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
import com.hits.coded.data.interfaces.ui.UIElementHandlesReorderingInterface
import com.hits.coded.data.interfaces.ui.UIElementSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.IfBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType
import com.hits.coded.databinding.ViewIfElseBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIIfElseBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UIElementHandlesDragAndDropInterface, UICodeBlockWithDataInterface,
    UICodeBlockWithLastTouchInformation, UICodeBlockElementHandlesDragAndDropInterface,
    UIElementSavesNestedBlocksInterface, UIElementHandlesCustomRemoveViewProcessInterface,
    UIElementHandlesReorderingInterface, UICodeBlockWithCustomRemoveViewProcessInterface,
    UICodeBlockSupportsErrorDisplaying {
    private val binding: ViewIfElseBlockBinding

    private val nestedBlocksAsBlockBase = ArrayList<BlockBase>()

    private val nestedElseBlocksAsBlockBase = ArrayList<BlockBase>()

    override val nestedUIBlocks: ArrayList<View?> = ArrayList()

    private var _block = IfBlock(IfBlockType.ONLY_IF)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    override var dropPosition: Int? = null

    init {
        inflate(
            context,
            R.layout.view_if_else_block,
            this
        ).also { view ->
            binding = ViewIfElseBlockBinding.bind(view)
        }

        initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()

        initConditionChangeListener()
    }

    override fun initDragAndDropListener() {
        binding.condition.setOnDragListener { _, dragEvent ->
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
                            handleConditionDropEvent(itemParent, draggableItem)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleConditionDragEndedEvent(draggableItem)

                            return@setOnDragListener true
                        }

                        else -> return@setOnDragListener false
                    }
                }
            }
            false
        }

        binding.nestedIfBlocksLayout.setOnDragListener { handlerView, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as? ViewGroup

            with(binding) {
                if (draggableItem as? UINestedableCodeBlock == null) {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_LOCATION -> {
                            handleDragLocationEvent(
                                nestedIfBlocksLayout,
                                itemParent,
                                handlerView,
                                dragEvent,
                                draggableItem
                            )

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            alphaMinusAnimation(backgroundTopImage)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            alphaPlusAnimation(backgroundTopImage)

                            clearAllNestedViewPaddings(nestedIfBlocksLayout)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(
                                this@UIIfElseBlock,
                                nestedIfBlocksLayout,
                                itemParent,
                                draggableItem,
                                { blockBase, position ->
                                    if (position != null) {
                                        nestedBlocksAsBlockBase.add(position, blockBase)
                                    } else {
                                        nestedBlocksAsBlockBase.add(blockBase)
                                    }

                                    _block.elseBlocks = nestedBlocksAsBlockBase.toTypedArray()
                                },
                                {
                                    alphaPlusAnimation(backgroundTopImage)
                                }
                            )

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(nestedIfBlocksLayout, itemParent, draggableItem)
                        }

                        else -> return@setOnDragListener false
                    }
                }
                false
            }
        }

        binding.nestedElseBlocksLayout.setOnDragListener { handlerView, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as? ViewGroup

            with(binding) {
                if (draggableItem as? UINestedableCodeBlock == null) {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_LOCATION -> {
                            handleDragLocationEvent(
                                nestedElseBlocksLayout,
                                itemParent,
                                handlerView,
                                dragEvent,
                                draggableItem
                            )

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            alphaMinusAnimation(backgroundBottomImage)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            alphaPlusAnimation(backgroundBottomImage)

                            clearAllNestedViewPaddings(nestedElseBlocksLayout)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(
                                this@UIIfElseBlock,
                                nestedElseBlocksLayout,
                                itemParent,
                                draggableItem,
                                { blockBase, position ->
                                    if (position != null) {
                                        nestedElseBlocksAsBlockBase.add(position, blockBase)
                                    } else {
                                        nestedElseBlocksAsBlockBase.add(blockBase)
                                    }

                                    _block.elseBlocks = nestedElseBlocksAsBlockBase.toTypedArray()
                                },
                                {
                                    alphaPlusAnimation(backgroundBottomImage)
                                }
                            )

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(nestedElseBlocksLayout, itemParent, draggableItem)
                        }

                        else -> return@setOnDragListener false
                    }
                }
                false
            }
        }
    }

    private fun handleConditionDropEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) = with(binding)
    {
        if (draggableItem != this@UIIfElseBlock) {
            scaleMinusAnimation(firstCard)

            itemParent.removeView(draggableItem)

            processViewWithCustomRemoveProcessRemoval(itemParent, draggableItem)

            condition.apply {
                setText("")
                visibility = INVISIBLE
            }

            clearNestedBlocksFromParent(firstCard)

            nestedUIBlocks.add(draggableItem)
            firstCard.addView(draggableItem)

            (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                _block.conditionBlock = it
            }
        }
    }

    private fun handleConditionDragEndedEvent(
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        if ((draggableItem as? UICodeBlockWithDataInterface)?.block == _block.conditionBlock) {
            draggableItem.x = 0f
            draggableItem.y = 0f
        }

        this.invalidate()
    }

    private fun initConditionChangeListener() =
        binding.condition.addTextChangedListener {
            _block.conditionBlock = it.toString()
        }

    override fun removeView(view: View?) {
        super.removeView(view)

        view?.tag = null

        nestedUIBlocks.remove(view)

        (view as? UICodeBlockWithDataInterface)?.block?.let {
            nestedBlocksAsBlockBase.remove(it)

            _block.nestedBlocks = nestedBlocksAsBlockBase.toTypedArray()
        }

        (view as? UINestedableCodeBlock)?.let {
            binding.condition.apply {
                setText("")
                visibility = VISIBLE
            }

            _block.conditionBlock = null
        }
    }

    override fun customRemoveView(view: View) {
        removeView(view)
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "IF_ELSE_BLOCK_"
    }

    override fun displayError() = with(binding) {
        backgroundTopImage.setImageResource(R.drawable.error_if_else_block_top_part)
        backgroundBottomImage.setImageResource(R.drawable.error_if_else_block_bottom_part)
    }

    override fun hideError() = with(binding) {
        backgroundTopImage.setImageResource(R.drawable.if_else_block_top_part)
        backgroundBottomImage.setImageResource(R.drawable.if_else_block_bottom_part)
    }
}