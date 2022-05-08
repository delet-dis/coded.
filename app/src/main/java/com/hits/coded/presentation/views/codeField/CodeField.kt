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
import com.hits.coded.data.interfaces.ui.UIElementSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.databinding.ViewCodeFieldBinding
import com.hits.coded.presentation.views.codeBlocks.start.UIActionStartBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    UIElementHandlesDragAndDropInterface, UIElementHandlesCustomRemoveViewProcessInterface,
    UIElementSavesNestedBlocksInterface {
    private val binding: ViewCodeFieldBinding

    private val startBlock = UIActionStartBlock(context)

    private var previousErrorBlock: UICodeBlockSupportsErrorDisplaying? = null

    var parentView: UIElementHandlesCodeBlocksDeletingInterface? = null

    private var savedXDropCoordinate = 0f
    private var savedYDropCoordinate = 0f

    override val nestedUIBlocks: ArrayList<View?> = ArrayList()

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

                DragEvent.ACTION_DRAG_STARTED -> {
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

                    if (nestedUIBlocks.contains(draggableItem)) {
                        draggableItem.x = savedXDropCoordinate
                        draggableItem.y = savedYDropCoordinate
                    }

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
            savedXDropCoordinate = dragEvent.x - (draggableItem.width / 2)
            savedYDropCoordinate = dragEvent.y - (draggableItem.height / 2)

            (draggableItem as? UICodeBlockWithLastTouchInformation)?.let {
                savedXDropCoordinate = dragEvent.x - it.touchX
                savedYDropCoordinate = dragEvent.y - it.touchY
            }

            itemParent?.let {
                processViewWithCustomRemoveProcessRemoval(it, draggableItem)
                it.removeView(draggableItem)
            }

            nestedUIBlocks.add(draggableItem)
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

                (processingView as? UIElementSavesNestedBlocksInterface)?.let {
                    it.nestedUIBlocks.forEach { block ->
                        currentId = block?.let { unwrappedBlock ->
                            calculateElementsIds(
                                unwrappedBlock,
                                currentId
                            )
                        }
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

    override fun removeView(view: View?) {
        super.removeView(view)

        nestedUIBlocks.remove(view)
    }

    fun hideError() =
        previousErrorBlock?.hideError()

    private companion object {
        const val VIEW_HIERARCHY_ID = "VIEW_HIERARCHY_ID_"
    }
}