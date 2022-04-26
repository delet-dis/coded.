package com.hits.coded.presentation.views.codeField

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.hits.coded.R
import com.hits.coded.data.models.codeField.CodeFieldInterface
import com.hits.coded.data.models.uiSharedInterfaces.UIElementHandlesDragNDropInterface
import com.hits.coded.databinding.ViewCodeFieldBinding
import com.hits.coded.presentation.views.codeBlocks.actions.UIActionStartBlock
import com.hits.coded.presentation.views.codeBlocks.variables.UIVariableCreationBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeField constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CodeFieldInterface,
    UIElementHandlesDragNDropInterface {
    private val binding: ViewCodeFieldBinding

    init {
        inflate(
            context,
            R.layout.view_code_field,
            this
        ).also { view ->
            binding = ViewCodeFieldBinding.bind(view)
        }
        initDragNDropListener()

        val firstBlock = UIVariableCreationBlock(context)
        firstBlock.tag = "treter"

        val secondBlock = UIVariableCreationBlock(context)
        secondBlock.tag = "treter"

        addBlock(UIActionStartBlock(context))
        addBlock(firstBlock)
        addBlock(secondBlock)
    }

    override fun addBlock(viewToAdd: View) {
        viewToAdd.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        binding.fieldLayout.addView(viewToAdd)

        viewToAdd.updateLayoutParams<LayoutParams> {
            topToTop = R.id.fieldLayout
            bottomToBottom = R.id.fieldLayout
            leftToLeft = R.id.fieldLayout
            rightToRight = R.id.fieldLayout
        }
    }

    override fun initDragNDropListener() {
        binding.fieldLayout.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as ViewGroup

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED,
                DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION,
                DragEvent.ACTION_DRAG_EXITED -> true

                DragEvent.ACTION_DROP -> {
                    handleDropEvent(itemParent, draggableItem, dragEvent)

                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    draggableItem.post { draggableItem.visibility = VISIBLE }

                    this.invalidate()
                    true
                }

                else -> false
            }
        }
    }

    private fun handleDropEvent(
        itemParent: ViewGroup,
        draggableItem: View,
        dragEvent: DragEvent
    ) =
        with(binding) {
            draggableItem.x = dragEvent.x - (draggableItem.width / 2)
            draggableItem.y = dragEvent.y - (draggableItem.height / 2)

            itemParent.removeView(draggableItem)

            addView(draggableItem)
        }
}