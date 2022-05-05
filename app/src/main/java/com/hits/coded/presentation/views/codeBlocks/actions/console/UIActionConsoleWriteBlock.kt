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
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
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
    UICodeBlockWithCustomRemoveViewProcessInterface,
    UIElementHandlesCustomRemoveViewProcessInterface, UICodeBlockSavesNestedBlocksInterface {
    private val binding: ViewConsoleWriteBlockBinding

    override val nestedUIBlocks: ArrayList<View> = ArrayList()

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

        initVariableNameChangeListener()
    }

    private fun initVariableNameChangeListener() =
        binding.variableName.addTextChangedListener {
            _block.argument = it.toString()
        }


    override fun initDragAndDropListener() {
        binding.variableName.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            (draggableItem as? UINestedableCodeBlock)?.let {
                val itemParent = draggableItem.parent as? ViewGroup

                itemParent?.let {
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED,
                        DragEvent.ACTION_DRAG_LOCATION -> return@setOnDragListener true

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            scalePlusAnimation(binding.firstCard)

                            return@setOnDragListener true
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            scaleMinusAnimation(binding.firstCard)

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
        if (draggableItem != this@UIActionConsoleWriteBlock) {
            scaleMinusAnimation(binding.firstCard)

            itemParent.removeView(draggableItem)

            processViewWithCustomRemoveProcessRemoval(itemParent, draggableItem)

            variableName.apply {
                setText("")
                visibility = INVISIBLE
            }

            clearNestedBlocksFromParent(firstCard)

            nestedUIBlocks.add(draggableItem)
            firstCard.addView(draggableItem)

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
        nestedUIBlocks.remove(view)
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