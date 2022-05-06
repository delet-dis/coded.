package com.hits.coded.presentation.views.codeField

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.UIElementHandlesCodeBlocksDeletingInterface
import com.hits.coded.data.interfaces.ui.UIElementHandlesCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.databinding.ViewCodeFieldBinding
import com.hits.coded.presentation.views.codeBlocks.actions.UIActionStartBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    UIElementHandlesDragAndDropInterface, UIElementHandlesCustomRemoveViewProcessInterface {
    private val binding: ViewCodeFieldBinding

    private val startBlock = UIActionStartBlock(context)

    private var previousErrorBlock: UICodeBlockSupportsErrorDisplaying? = null

    var parentView: UIElementHandlesCodeBlocksDeletingInterface? = null

    init {
        inflate(
            context,
            R.layout.view_code_field,
            this
        ).also { view ->
            binding = ViewCodeFieldBinding.bind(view)
        }
        initDragAndDropListener()

        addBlock(startBlock)
    }

    private fun addBlock(viewToAdd: View) {
        viewToAdd.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        binding.fieldLayout.addView(viewToAdd)

        viewToAdd.updateLayoutParams<LayoutParams> {
            topToTop = R.id.fieldLayout
            bottomToBottom = R.id.fieldLayout
            leftToLeft = R.id.fieldLayout
            rightToRight = R.id.fieldLayout
        }
    }

    override fun initDragAndDropListener() =
        binding.fieldLayout.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as? ViewGroup

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION,
                DragEvent.ACTION_DRAG_EXITED -> true

                DragEvent.ACTION_DRAG_STARTED->{
                    parentView?.startDeleting()

                    true
                }

                DragEvent.ACTION_DROP -> {
                    handleDropEvent(itemParent, draggableItem, dragEvent)

                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    draggableItem.post {
                        draggableItem.animate().alpha(1f).duration =
                            UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
                    }

                    parentView?.stopDeleting()

                    this.invalidate()
                    true
                }

                else -> false
            }
        }

    private fun handleDropEvent(
        itemParent: ViewGroup?,
        draggableItem: View,
        dragEvent: DragEvent
    ) =
        with(binding) {
            val draggableItemWithLastTouchInformation =
                draggableItem as? UICodeBlockWithLastTouchInformation

            draggableItem.x = dragEvent.x - (draggableItem.width / 2)
            draggableItem.y = dragEvent.y - (draggableItem.height / 2)

            draggableItemWithLastTouchInformation?.let {
                draggableItem.x = dragEvent.x - (it.touchX)
                draggableItem.y = dragEvent.y - (it.touchY)
            }

            itemParent?.let {
                processViewWithCustomRemoveProcessRemoval(it, draggableItem)
                it.removeView(draggableItem)
            }

            addView(draggableItem)
        }

    fun calculateBlocksHierarchyIds() =
        calculateElementsIds(startBlock)

    fun getEntryPointBlock() = startBlock

    private fun calculateElementsIds(
        processingView: View,
        previousId: Int? = -1
    ): Int? {
        var currentId: Int?

        (processingView as? UICodeBlockWithDataInterface)?.let {
            if (previousId != null) {
                currentId = previousId + 1

                processingView.block.id = currentId

                processingView.tag = VIEW_HIERARCHY_ID + currentId

                (processingView as? UICodeBlockSavesNestedBlocksInterface)?.let {
                    it.nestedUIBlocks.forEach { block ->
                        currentId = calculateElementsIds(block, currentId)
                    }
                }
                return currentId
            }
        }
        return null
    }

    fun displayError(blockId: Int) {
        hideError()

        val foundedView =
            findViewWithTag<View>(VIEW_HIERARCHY_ID + blockId) as? UICodeBlockSupportsErrorDisplaying

        foundedView?.displayError()

        previousErrorBlock = foundedView
    }

    fun hideError() =
        previousErrorBlock?.hideError()

    private companion object {
        const val VIEW_HIERARCHY_ID = "VIEW_HIERARCHY_ID_"
    }
}