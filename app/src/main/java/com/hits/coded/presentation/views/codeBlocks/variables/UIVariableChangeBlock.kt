package com.hits.coded.presentation.views.codeBlocks.variables

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
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.databinding.ViewVariableChangeBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIVariableChangeBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface,
    UIElementHandlesCustomRemoveViewProcessInterface, UICodeBlockSavesNestedBlocksInterface,
    UICodeBlockSupportsErrorDisplaying {
    private val binding: ViewVariableChangeBlockBinding

    override val nestedUIBlocks: ArrayList<View?> = ArrayList()

    private var variableParams = StoredVariable()

    private var _block = VariableBlock(null, variableParams)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    var blockType: VariableBlockType? = null
        set(value) {
            field = value

            value?.let {
                changeBlockType(it)
            }
        }

    init {
        inflate(
            context,
            R.layout.view_variable_change_block,
            this
        ).also { view ->
            binding = ViewVariableChangeBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()

        initVariableNameChangeListener()

        initVariableChangeValueListener()
    }

    private fun changeBlockType(blockType: VariableBlockType) {
        _block.variableBlockType = blockType
        when (blockType) {
            VariableBlockType.VARIABLE_SET -> {
                binding.firstText.setText(R.string.set)
                binding.secondText.setText(R.string.to)
            }

            VariableBlockType.VARIABLE_CHANGE -> {
                binding.firstText.setText(R.string.change)
                binding.secondText.setText(R.string.by)
            }

            else -> {}
        }
    }

    private fun initVariableNameChangeListener() =
        binding.variableName.addTextChangedListener {
            variableParams.name = it.toString()
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

        binding.variableChangeValue.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            (draggableItem as? UINestedableCodeBlock)?.let {
                val itemParent = draggableItem.parent as? ViewGroup

                itemParent?.let {
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
            }
            false
        }
    }

    private fun handleDropEvent(
        itemParent: ViewGroup,
        draggableItem: View
    ) = with(binding) {
        if (draggableItem != this@UIVariableChangeBlock) {
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

        view.tag = null

        _block.valueToSet = null

        binding.variableChangeValue.apply {
            setText("")
            visibility = VISIBLE
        }
    }

    override fun hideError() =
        binding.backgroundImage.setImageResource(R.drawable.variable_block)

    override fun displayError() =
        binding.backgroundImage.setImageResource(R.drawable.error_block)

    private companion object {
        const val DRAG_AND_DROP_TAG = "VARIABLE_CHANGE_BY_BLOCK_"
    }
}