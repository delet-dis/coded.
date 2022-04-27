package com.hits.coded.presentation.views.codeBlocks.variables

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockWithDataInterface
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UIMoveableCodeBlockInterface
import com.hits.coded.data.models.uiSharedInterfaces.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.models.uiSharedInterfaces.UIElementHandlesDragAndDropInterface
import com.hits.coded.databinding.ViewVariableChangeByBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIVariableChangeByBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface {
    private val binding: ViewVariableChangeByBlockBinding

    private var variableParams = StoredVariable()

    private var _block = VariableBlock(VariableBlockType.VARIABLE_CHANGE, variableParams)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    override val animationSet = AnimatorSet()

    init {
        inflate(
            context,
            R.layout.view_variable_change_by_block,
            this
        ).also { view ->
            binding = ViewVariableChangeByBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initVariableNameChangeListener()

        initVariableChangeValueListener()
    }

    private fun initVariableNameChangeListener() = binding.variableName.addTextChangedListener {
        variableParams.name = it.toString()

        _block.variableParams = variableParams
    }

    private fun initVariableChangeValueListener() =
        binding.variableChangeValue.addTextChangedListener {
            _block.valueToSet = it.toString()
        }

    override fun initDragAndDropListener() {
        binding.secondCard.setOnDragListener { view, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as ViewGroup

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED,
                DragEvent.ACTION_DRAG_LOCATION -> true

                DragEvent.ACTION_DRAG_ENTERED -> {
                    scalePlusAnimation(binding.parentConstraint)

                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    scaleMinusAnimation(binding.parentConstraint)

                    true
                }

                DragEvent.ACTION_DROP -> {
                    handleDropEvent(itemParent, draggableItem)

                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
//                    handleDragEndedEvent(itemParent, draggableItem)

                    true
                }

                else -> false
            }
        }
    }

    private fun handleDropEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) =
        with(binding) {
            if (draggableItem != this@UIVariableChangeByBlock) {
                scaleMinusAnimation(parentConstraint)

                itemParent.removeView(draggableItem)

                variableChangeValue.apply {
                    visibility = View.INVISIBLE
                    setText("")
                }

                secondCard.addView(draggableItem)

                (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                    _block.valueToSet = it
                }
            }
        }

    override fun removeView(view: View?) {
        super.removeView(view)

        binding.variableChangeValue.visibility = View.VISIBLE
        _block.valueToSet = ""
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "VARIABLE_CHANGE_BY_BLOCK_"
    }
}