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
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.IfBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IfBlockType
import com.hits.coded.databinding.ViewIfBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIIfBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UIElementHandlesDragAndDropInterface, UICodeBlockWithDataInterface,
    UICodeBlockWithLastTouchInformation, UICodeBlockElementHandlesDragAndDropInterface,
    UIElementSavesNestedBlocksInterface, UIElementHandlesCustomRemoveViewProcessInterface,
    UIElementHandlesReorderingInterface, UICodeBlockWithCustomRemoveViewProcessInterface {
    private val binding: ViewIfBlockBinding

    private val nestedBlocksAsBlockBase = ArrayList<BlockBase>()

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
            R.layout.view_if_block,
            this
        ).also { view ->
            binding = ViewIfBlockBinding.bind(view)
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

        binding.parentConstraint.setOnDragListener { handlerView, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as? ViewGroup

            with(binding) {
                if (draggableItem as? UINestedableCodeBlock == null) {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_LOCATION -> {
                            handleDragLocationEvent(
                                nestedBlocksLayout,
                                itemParent,
                                handlerView,
                                dragEvent,
                                draggableItem
                            )

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            scalePlusAnimation(parentConstraint)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            scaleMinusAnimation(parentConstraint)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(
                                this@UIIfBlock,
                                binding.nestedBlocksLayout,
                                itemParent,
                                draggableItem
                            ) {
                                nestedBlocksAsBlockBase.add(it)

                                _block.nestedBlocks = nestedBlocksAsBlockBase.toTypedArray()
                            }

                            (this@UIIfBlock.parent as View).invalidate()

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(nestedBlocksLayout, itemParent, draggableItem)
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
        if (draggableItem != this@UIIfBlock) {
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
    }

    override fun customRemoveView(view: View) {
        removeView(view)
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "IF_BLOCK_"
    }
}