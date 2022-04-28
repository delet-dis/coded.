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
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockWithDataInterface
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UIMoveableCodeBlockInterface
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
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface {
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

            if(draggableItem == draggableItem){
                //TODO: Добавить проверку на то, закидывается ли условие или выражение

                val itemParent = draggableItem.parent as ViewGroup

                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_STARTED,
                    DragEvent.ACTION_DRAG_LOCATION -> return@setOnDragListener true

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        alphaMinusAnimation(binding.root)

                        return@setOnDragListener true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        alphaPlusAnimation(binding.root)

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
    ) =
        with(binding) {
            if (draggableItem != this@UIVariableChangeByBlock) {
                alphaPlusAnimation(parentConstraint)

                itemParent.removeView(draggableItem)

                variableChangeValue.apply {
                    setText("")
                    visibility = INVISIBLE
                }

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
        binding.secondCard.removeView(view)

        _block.valueToSet = null

        binding.variableChangeValue.apply {
            setText("")
            visibility = VISIBLE
        }
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "VARIABLE_CHANGE_BY_BLOCK_"
    }
}