package com.hits.coded.presentation.views.codeBlocks.actions.console

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.databinding.ViewConsoleWriteBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIActionConsoleWriteBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface {
    private val binding: ViewConsoleWriteBlockBinding

    private var variableParams = StoredVariable()

    private var _block = IOBlock(IOBlockType.WRITE)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    override val animationSet = AnimatorSet()

    init {
        inflate(
            context,
            R.layout.view_console_write_block,
            this
        ).also { view ->
            binding = ViewConsoleWriteBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initDragAndDropListener()
    }

    private fun initVariableNameChangeListener() =
        binding.variableName.addTextChangedListener {
            variableParams.name = it.toString()

            _block.argument = variableParams
        }


    override fun initDragAndDropListener() {
        binding.variableName.setOnDragListener { _, _ ->
            true
        }

        binding.parentConstraint.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            if (draggableItem == draggableItem) {
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
    ) = with(binding) {
        if (draggableItem != this@UIActionConsoleWriteBlock) {
            alphaPlusAnimation(parentConstraint)

            itemParent.removeView(draggableItem)

            variableName.apply {
                setText("")
                visibility = INVISIBLE
            }

            binding.firstCard.addView(draggableItem)

            (draggableItem as? UICodeBlockWithDataInterface)?.block?.let {
                _block.argument = it
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

        if ((draggableItem as? UICodeBlockWithDataInterface)?.block == _block.argument) {
            draggableItem.x = 0f
            draggableItem.y = 0f
        }

        this.invalidate()
    }

    override fun customRemoveView(view: View) {
        binding.firstCard.removeView(view)

        _block.argument = null

        with(binding.variableName) {
            setText("")
            visibility = VISIBLE
        }
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "ACTION_CONSOLE_WRITE_BLOCK_"
    }
}