package com.hits.coded.data.interfaces.ui.codeBlocks

import android.view.View
import android.view.ViewGroup

interface UICodeBlockWithNestedBlocksAndConditionInterface {
    fun handleConditionDropEvent(itemParent: ViewGroup, draggableItem: View)
    fun handleConditionDragEndedEvent(draggableItem: View)
    fun initConditionChangeListener()
}