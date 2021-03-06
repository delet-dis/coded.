package com.hits.coded.presentation.views.codeBlocks.shadowBuilder

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class BlockDragShadowBuilder(view: View, private val touchX: Int, private val touchY: Int) :
    View.DragShadowBuilder(view) {
    private val shadow = view

    override fun onDrawShadow(canvas: Canvas?) =
        shadow.draw(canvas)

    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        val height: Int = view.height
        val width: Int = view.width

        outShadowSize?.set(width, height)

        outShadowTouchPoint?.set(touchX, touchY)
    }
}