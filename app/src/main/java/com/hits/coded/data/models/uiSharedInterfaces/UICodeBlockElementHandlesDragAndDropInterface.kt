package com.hits.coded.data.models.uiSharedInterfaces

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

interface UICodeBlockElementHandlesDragAndDropInterface{
    val animationSet: AnimatorSet

    fun scalePlusAnimation(view: View) {
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f)

        animationSet.apply {
            playTogether(scaleX, scaleY)
            duration = 200
            start()
        }
    }

    fun scaleMinusAnimation(view: View) {
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1f)

        animationSet.apply {
            playTogether(scaleX, scaleY)
            duration = 200
            start()
        }
    }

    companion object{
        const val ANIMATION_DURATION = "ANIAMTION_DURATION"
    }
}