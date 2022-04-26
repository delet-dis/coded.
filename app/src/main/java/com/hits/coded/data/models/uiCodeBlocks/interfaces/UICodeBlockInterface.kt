package com.hits.coded.data.models.uiCodeBlocks.interfaces

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.View
import com.hits.coded.presentation.views.codeBlocks.shadowBuilder.BlockDragShadowBuilder
import kotlin.random.Random

interface UICodeBlockInterface {
    fun initDragNDropGesture(view: View, tag: String) {
        view.tag = tag + Random.Default.nextInt().toString()

        view.setOnLongClickListener {
            val item = ClipData.Item(tag as? CharSequence)

            val dataToDrag = ClipData(
                tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val maskShadow = BlockDragShadowBuilder(view)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                it.startDrag(dataToDrag, maskShadow, this, 0)
            } else {
                it.startDragAndDrop(dataToDrag, maskShadow, this, 0)
            }

            view.visibility = View.INVISIBLE

            true
        }
    }
}
