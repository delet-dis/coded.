package com.hits.coded.data.interfaces.ui.codeBlocks

import android.view.View
import android.view.ViewGroup

interface UICodeBlockSavesNestedBlocksInterface {
    val nestedUIBlocks: ArrayList<View>

    fun clearNestedBlocksFromParent(parent: ViewGroup) =
        nestedUIBlocks.forEach {
            parent.removeView(it)
        }
}