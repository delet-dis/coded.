package com.hits.coded.data.interfaces.ui

import android.view.View
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface

interface UIElementHandlesCustomRemoveViewProcessInterface {
    fun processViewWithCustomRemoveProcessRemoval(itemParent: View, draggableItem: View) {
        var currentParent = itemParent.parent

        while (currentParent != null) {
            val currentParentConverted =
                currentParent as? UICodeBlockWithCustomRemoveViewProcessInterface

            if (currentParentConverted != null) {
                currentParentConverted.customRemoveView(
                    draggableItem
                )

                break
            }

            currentParent = currentParent.parent
        }
    }
}