package com.hits.coded.data.interfaces.ui

import android.graphics.Rect
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.contains
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.domain.extensions.getTouchPositionFromDragEvent

interface UIElementHandlesReorderingInterface : UIElementSavesNestedBlocksInterface,
    UIElementHandlesCustomRemoveViewProcessInterface,
    UICodeBlockElementHandlesDragAndDropInterface {
    var dropPosition: Int?

    fun handleDragLocationEvent(
        listLinearLayout: LinearLayout,
        itemParent: ViewGroup?,
        handlerView: View,
        dragEvent: DragEvent,
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(0f).duration =
                UIMoveableCodeBlockInterface.ITEM_DISAPPEAR_ANIMATION_DURATION
        }

        if (itemParent?.contains(draggableItem) == true) {
            processViewWithCustomRemoveProcessRemoval(itemParent, draggableItem)
            itemParent.removeView(draggableItem)
        }

        dropPosition?.let {
            listLinearLayout.getChildAt(it)
                ?.setPadding(0, 0, 0, 0)
        }

        dropPosition = checkForDraggableElementIntersectionWithLinearLayoutElements(
            listLinearLayout, handlerView, dragEvent
        )

        dropPosition?.let {
            val intersectionView = listLinearLayout.getChildAt(it)

            if (draggableItem != intersectionView) {
                intersectionView?.setPadding(0, draggableItem.height, 0, 0)
            }
        }
    }

    fun checkForDraggableElementIntersectionWithLinearLayoutElements(
        listLinearLayout: LinearLayout,
        viewToCheck: View,
        eventToCheck: DragEvent
    ): Int? {
        val touchPosition = getTouchPositionFromDragEvent(viewToCheck, eventToCheck)

        val eventRect = Rect(
            touchPosition.x,
            touchPosition.y,
            (touchPosition.x + 1),
            (touchPosition.y + 1)
        )

        listLinearLayout.children.forEachIndexed { position, item ->
            if (viewToCheck != item) {
                val elementRect = Rect()
                item.getGlobalVisibleRect(elementRect)

                if (eventRect.intersect(elementRect)) {
                    return position
                }
            }
        }
        return null
    }

    fun handleDragEndedEvent(
        listLinearLayout: LinearLayout,
        itemParent: ViewGroup?,
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        with(listLinearLayout) {
            if (itemParent == this) {
                draggableItem.x = 0f

                if (dropPosition != null) {
                    val childRect = Rect()
                    this.getChildAt(dropPosition!!)
                        ?.getDrawingRect(childRect)

                    this.offsetDescendantRectToMyCoords(
                        getChildAt(
                            dropPosition!!
                        ), childRect
                    )

                    draggableItem.y = childRect.top.toFloat()
                } else {

                    if (this.childCount.minus(1) == 0) {
                        draggableItem.y = 0f
                    } else {
                        val childRect = Rect()
                        this.getChildAt(childCount - 1)
                            ?.getDrawingRect(childRect)

                        this.offsetDescendantRectToMyCoords(
                            getChildAt(
                                childCount - 1
                            ), childRect
                        )

                        draggableItem.y = childRect.top.toFloat()
                    }
                }
            }
        }
    }

    fun handleDropEvent(
        block: View,
        listLinearLayout: LinearLayout,
        itemParent: ViewGroup?,
        draggableItem: View,
        blockAddClosure: (BlockBase) -> Unit
    ) {
        if (draggableItem != block) {
            scaleMinusAnimation(listLinearLayout.parent as View)

            if (dropPosition != null) {
                listLinearLayout.getChildAt(dropPosition!!)?.setPadding(0, 0, 0, 0)

                if (draggableItem != listLinearLayout.getChildAt(dropPosition!!)) {
                    itemParent?.let {
                        processViewWithCustomRemoveProcessRemoval(it, draggableItem)
                    }
                    itemParent?.removeView(draggableItem)

                    nestedUIBlocks.add(dropPosition!!, draggableItem)
                    listLinearLayout.addView(draggableItem, dropPosition!!)

                    (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                        blockAddClosure(it)
                    }
                }
            } else {
                itemParent?.let { processViewWithCustomRemoveProcessRemoval(it, draggableItem) }
                itemParent?.removeView(draggableItem)

                nestedUIBlocks.add(draggableItem)
                listLinearLayout.addView(draggableItem)

                (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                    blockAddClosure(it)
                }
            }
        }
    }
}