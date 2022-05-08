package com.hits.coded.data.interfaces.ui

import android.graphics.Rect
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.contains
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.domain.extensions.getTouchPositionFromDragEvent

interface UIElementHandlesReorderingInterface : UIElementSavesNestedBlocksInterface,
    UIElementHandlesCustomRemoveViewProcessInterface {
    var layoutListView: LinearLayout?

    var dropPosition: Int?

    fun handleDragLocationEvent(
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
            layoutListView?.getChildAt(it)
                ?.setPadding(0, 0, 0, 0)
        }

        dropPosition = checkForDraggableElementIntersectionWithLinearLayoutElements(
            handlerView, dragEvent
        )

        dropPosition?.let {
            val intersectionView = layoutListView?.getChildAt(it)

            if (draggableItem != intersectionView) {
                intersectionView?.setPadding(0, draggableItem.height, 0, 0)
            }
        }
    }

    fun checkForDraggableElementIntersectionWithLinearLayoutElements(
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

        layoutListView?.children?.forEachIndexed { position, item ->
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
        itemParent: ViewGroup?,
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        with(layoutListView) {
            if (itemParent == this) {
                draggableItem.x = 0f

                if (dropPosition != null) {
                    val childRect = Rect()
                    this?.getChildAt(dropPosition!!)
                        ?.getDrawingRect(childRect)

                    this?.offsetDescendantRectToMyCoords(
                        getChildAt(
                            dropPosition!!
                        ), childRect
                    )

                    draggableItem.y = childRect.top.toFloat()
                } else {

                    if (this?.childCount?.minus(1) == 0) {
                        draggableItem.y = 0f
                    } else {
                        val childRect = Rect()
                        this?.getChildAt(childCount - 1)
                            ?.getDrawingRect(childRect)

                        this?.offsetDescendantRectToMyCoords(
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
}