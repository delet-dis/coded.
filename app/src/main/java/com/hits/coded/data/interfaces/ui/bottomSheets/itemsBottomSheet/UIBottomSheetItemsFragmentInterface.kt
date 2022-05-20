package com.hits.coded.data.interfaces.ui.bottomSheets.itemsBottomSheet

import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView

interface UIBottomSheetItemsFragmentInterface {
    fun redrawElements()

    fun getBlockInHorizontalScrollView(view: View): View =
        HorizontalScrollView(view.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            addView(view)
        }
}