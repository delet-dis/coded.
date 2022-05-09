package com.hits.coded.presentation.views.codeBlocks.start

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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
    UICodeBlockWithLastTouchInformation,
    UICodeBlockElementHandlesDragAndDropInterface,
    UIElementSavesNestedBlocksInterface, UIElementHandlesCustomRemoveViewProcessInterface,
    UIElementHandlesReorderingInterface, UICodeBlockWithCustomRemoveViewProcessInterface {
    private val binding: ViewActionStartBinding

    private val nestedBlocksAsBlockBase = ArrayList<BlockBase>()

    override val nestedUIBlocks: ArrayList<View?> = ArrayList()

    private var _block = StartBlock()
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    override var dropPosition: Int? = null

    init {
        inflate(
            context,
            R.layout.view_action_start,
            this
        ).also { view ->
            binding = ViewActionStartBinding.bind(view)
        }

        initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()
    }

    override fun initDragAndDropListener() =
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
                            alphaMinusAnimation(binding.backgroundImage)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            alphaPlusAnimation(binding.backgroundImage)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DROP -> {
                            handleDropEvent(
                                this@UIActionStartBlock,
                                binding.nestedBlocksLayout,
                                itemParent,
                                draggableItem,
                                {
                                    nestedBlocksAsBlockBase.add(it)

                                    _block.nestedBlocks = nestedBlocksAsBlockBase.toTypedArray()
                                },
                                {
                                     alphaPlusAnimation(binding.backgroundImage)
                                }
                            )

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            handleDragEndedEvent(nestedBlocksLayout, itemParent, draggableItem)

                            return@setOnDragListener true
                        }

                        else -> return@setOnDragListener false
                    }
                }
                false
            }
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
        const val DRAG_AND_DROP_TAG = "ACTION_START_BLOCK_"
    }
}