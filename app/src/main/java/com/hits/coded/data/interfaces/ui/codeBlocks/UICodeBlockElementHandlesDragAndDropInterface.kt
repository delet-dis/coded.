package com.hits.coded.data.interfaces.ui.codeBlocks

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

interface UICodeBlockElementHandlesDragAndDropInterface {
    fun scalePlusAnimation(view: View) {
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", SCALE_START, SCALE_END)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", SCALE_START, SCALE_END)

        playAnimations(scaleX, scaleY)
    }

    fun scaleMinusAnimation(view: View) {
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", SCALE_END, SCALE_START)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", SCALE_END, SCALE_START)

        playAnimations(scaleX, scaleY)
    }

    fun alphaPlusAnimation(view: View) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", ALPHA_START, ALPHA_END)

        playAnimations(alpha)
    }

    fun alphaMinusAnimation(view: View) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", ALPHA_END, ALPHA_START)

        playAnimations(alpha)
    }

    fun playAnimations(vararg items: Animator) =
        AnimatorSet().apply {
            playTogether(*items)
            duration = ANIMATION_DURATION
            start()
        }

    companion object {
        const val ANIMATION_DURATION: Long = 200

        const val ALPHA_END = 1f
        const val ALPHA_START = 0.5f

        const val SCALE_END = 1.3f
        const val SCALE_START = 1f
    }
}