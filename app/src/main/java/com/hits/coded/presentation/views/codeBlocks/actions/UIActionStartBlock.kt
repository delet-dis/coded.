package com.hits.coded.presentation.views.codeBlocks.actions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.data.models.uiCodeBLocks.UICodeBlockHandlesDragNDropInterface
import com.hits.coded.data.models.uiCodeBLocks.UICodeBlockInterface
import com.hits.coded.databinding.ViewActionStartBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UIActionStartBlock constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UICodeBlockInterface,
    UICodeBlockHandlesDragNDropInterface {
    private val binding: ViewActionStartBinding

    private val animationSet = AnimatorSet()

    init {
        inflate(
            context,
            R.layout.view_action_start,
            this
        ).also { view ->
            binding = ViewActionStartBinding.bind(view)
        }

        this.initDragNDropGesture(this, DRAG_N_DROP_TAG)

        initDragNDropListener()
    }

    private companion object {
        const val DRAG_N_DROP_TAG = "ACTION_START_BLOCK_"
    }

    override fun initDragNDropListener() {
        binding.parentConstraint.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as ViewGroup

            with(binding) {
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_STARTED,
                    DragEvent.ACTION_DRAG_LOCATION -> true

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        scalePlusAnimation(binding.parentConstraint)

                        true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        scaleMinusAnimation(parentConstraint)

                        true
                    }

                    DragEvent.ACTION_DROP -> {
                        scaleMinusAnimation(parentConstraint)

                        itemParent.removeView(draggableItem)

                        nestedBlocks.addView(draggableItem)

                        true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        draggableItem.post { draggableItem.visibility = VISIBLE }

                        this@UIActionStartBlock.invalidate()

                        if (itemParent == binding.nestedBlocks) {
                            draggableItem.x = 0f
                            if (binding.nestedBlocks.childCount == 0) {
                                draggableItem.y = 0f
                            } else {
                                val childRect = Rect()
                                binding.nestedBlocks.getChildAt(binding.nestedBlocks.childCount-1).getDrawingRect(childRect)

                                binding.nestedBlocks.offsetDescendantRectToMyCoords(binding.nestedBlocks.getChildAt(binding.nestedBlocks.childCount-1), childRect)

                                draggableItem.y = childRect.top.toFloat()
                            }
                        }

                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun scalePlusAnimation(view: View) {
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f)

        animationSet.apply {
            playTogether(scaleX, scaleY)
            duration = 200
            start()
        }
    }

    private fun scaleMinusAnimation(view: View) {
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1f)

        animationSet.apply {
            playTogether(scaleX, scaleY)
            duration = 200
            start()
        }
    }
}