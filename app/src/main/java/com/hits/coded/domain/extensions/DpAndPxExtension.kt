package com.hits.coded.domain.extensions

import android.content.Context
import android.util.DisplayMetrics

fun Int.pxToDp(context: Context) =
    this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Int.dpToPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()