package com.hits.coded.domain.extensions

import android.graphics.Point
import android.graphics.Rect
import android.view.DragEvent
import android.view.View
import kotlin.math.roundToInt


fun getTouchPositionFromDragEvent(item: View, event: DragEvent): Point {
    val itemRect = Rect()
    item.getGlobalVisibleRect(itemRect)
    return Point(itemRect.left + event.x.roundToInt(), itemRect.top + event.y.roundToInt())
}