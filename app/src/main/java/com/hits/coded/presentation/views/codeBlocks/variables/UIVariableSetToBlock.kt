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
import com.hits.coded.data.interfaces.ui.UIElementHandlesCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.databinding.ViewVariableSetToBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIVariableSetToBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface,
    UIElementHandlesCustomRemoveViewProcessInterface, UICodeBlockSavesNestedBlocksInterface {
    private val binding: ViewVariableSetToBlockBinding

    override val nestedUIBlocks: ArrayList<View> = ArrayList()

    private var variableParams = StoredVariable()

    private var _block = VariableBlock(VariableBlockType.VARIABLE_SET, variableParams)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    override val animationSet = AnimatorSet()

    init {
        inflate(
            context,
            R.layout.view_variable_set_to_block,
            this
        ).also { view ->
            binding = ViewVariableSetToBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()

        initVariableNameChangeListener()

        initVariableChangeValueListener()
    }

    private fun initVariableNameChangeListener() =
        binding.variableName.addTextChangedListener {
            variableParams.name = it.toString()

            _block.variableParams = variableParams
        }

    private fun initVariableChangeValueListener() =
        binding.variableChangeValue.addTextChangedListener {
            _block.valueToSet = it.toString()
        }

    override fun initDragAndDropListener() {
        binding.variableChangeValue.setOnDragListener { _, _ ->
            true
        }

        binding.variableName.setOnDragListener { _, _ ->
            true
        }

        binding.parentConstraint.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            (draggableItem as? UINestedableCodeBlock)?.let {
                val itemParent = draggableItem.parent as ViewGroup

                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_STARTED,
                    DragEvent.ACTION_DRAG_LOCATION -> return@setOnDragListener true

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        scalePlusAnimation(binding.secondCard)

                        return@setOnDragListener true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        scaleMinusAnimation(binding.secondCard)

                        return@setOnDragListener true
                    }

                    DragEvent.ACTION_DROP -> {
                        handleDropEvent(itemParent, draggableItem)

                        return@setOnDragListener true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        handleDragEndedEvent(draggableItem)

                        return@setOnDragListener true
                    }

                    else -> return@setOnDragListener false
                }
            }
            false
        }
    }

    private fun handleDropEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) = with(binding) {
        if (draggableItem != this@UIVariableSetToBlock) {
            scaleMinusAnimation(binding.secondCard)

            itemParent.removeView(draggableItem)

            processViewWithCustomRemoveProcessRemoval(itemParent, draggableItem)

            variableChangeValue.apply {
                setText("")
                visibility = INVISIBLE
            }

            clearNestedBlocksFromParent(secondCard)

            nestedUIBlocks.add(draggableItem)
            secondCard.addView(draggableItem)

            (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                _block.valueToSet = it
            }
        }
    }

    private fun handleDragEndedEvent(
        draggableItem: View
    ) {
        draggableItem.post {
            draggableItem.animate().alpha(1f).duration =
                UIMoveableCodeBlockInterface.ITEM_APPEAR_ANIMATION_DURATION
        }

        if ((draggableItem as? UICodeBlockWithDataInterface)?.block == _block.valueToSet) {
            draggableItem.x = 0f
            draggableItem.y = 0f
        }

        this.invalidate()
    }

    override fun customRemoveView(view: View) {
        nestedUIBlocks.remove(view)
        binding.secondCard.removeView(view)

        _block.valueToSet = null

        binding.variableChangeValue.apply {
            setText("")
            visibility = VISIBLE
        }
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "VARIABLE_SET_TO_BLOCK_"
    }
}