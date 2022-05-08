package com.hits.coded.data.interfaces.ui

import android.view.View
import android.view.ViewGroup

interface UIElementSavesNestedBlocksInterface {
    val nestedUIBlocks: ArrayList<View?>

    fun clearNestedBlocksFromParent(parent: ViewGroup) {
        nestedUIBlocks.forEach {
            parent.removeView(it)
        }

        nestedUIBlocks.clear()
    }
}