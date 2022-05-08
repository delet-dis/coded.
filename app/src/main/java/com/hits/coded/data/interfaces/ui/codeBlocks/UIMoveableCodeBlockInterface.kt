package com.hits.coded.data.interfaces.ui.codeBlocks

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.MotionEvent
import android.view.View
import com.hits.coded.presentation.views.codeBlocks.shadowBuilder.BlockDragShadowBuilder

interface UIMoveableCodeBlockInterface {
    @SuppressLint("ClickableViewAccessibility")
    fun initDragAndDropGesture(view: View, tag: String) {
        view.tag = tag + (0..MAXIMUM_RANDOM_NUMBER).random()

        var touchX = 0
        var touchY = 0

        view.setOnTouchListener { _, motionEvent ->
            if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                touchX = motionEvent.x.toInt()
                touchY = motionEvent.y.toInt()
            }

            false
        }

        view.setOnLongClickListener {
            val item = ClipData.Item(tag as? CharSequence)

            val dataToDrag = ClipData(
                tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            (it as? UICodeBlockWithLastTouchInformation)?.setLastTouchInformation(touchX, touchY)

            val maskShadow = BlockDragShadowBuilder(view, touchX, touchY)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                it.startDrag(dataToDrag, maskShadow, this, 0)
            } else {
                it.startDragAndDrop(dataToDrag, maskShadow, this, 0)
            }

            view.animate().alpha(0f).duration = ITEM_DISAPPEAR_ANIMATION_DURATION

            true
        }
    }

    companion object {
        const val ITEM_APPEAR_ANIMATION_DURATION: Long = 400
        const val ITEM_DISAPPEAR_ANIMATION_DURATION: Long = 500

        const val MAXIMUM_RANDOM_NUMBER = 100
    }
}
