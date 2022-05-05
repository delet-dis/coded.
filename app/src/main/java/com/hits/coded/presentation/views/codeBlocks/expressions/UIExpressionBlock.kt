package com.hits.coded.presentation.views.codeBlocks.expressions

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
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
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.ExpressionBlockType
import com.hits.coded.databinding.ViewExpressionBlockBinding

class UIExpressionBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
    UICodeBlockWithCustomRemoveViewProcessInterface,
    UIElementHandlesCustomRemoveViewProcessInterface, UICodeBlockSavesNestedBlocksInterface,
    UINestedableCodeBlock {
    private val binding: ViewExpressionBlockBinding

    override val nestedUIBlocks: ArrayList<View> = ArrayList()

    private var leftSide: Any? = Any()
        set(value) {
            field = value

            _block.leftSide = value
        }
    private var rightSide: Any? = Any()
        set(value) {
            field = value

            _block.rightSide = value
        }

    private var _block = ExpressionBlock()
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0
    override val animationSet: AnimatorSet = AnimatorSet()

    var blockType: ExpressionBlockType? = null
        set(value) {
            field = value

            value?.let {
                changeBlockType(it)
            }
        }

    init {
        inflate(
            context,
            R.layout.view_expression_block,
            this
        ).also { view ->
            binding = ViewExpressionBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initCardsTextsListeners()
    }

    private fun changeBlockType(blockType: ExpressionBlockType) {
        _block.expressionBlockType = blockType

        binding.centerText.setText(blockType.resourceId)
    }

    private fun initCardsTextsListeners() {
        binding.leftCardText.addTextChangedListener {
            leftSide = it.toString()
        }

        binding.rightCardText.addTextChangedListener {
            leftSide = it.toString()
        }
    }

    override fun initDragAndDropListener() {
        TODO("Not yet implemented")
    }

    override fun customRemoveView(view: View) {
        nestedUIBlocks.remove(view)

        binding.leftCard.removeView(view)
        binding.rightCard.removeView(view)

        val removingViewBlock = (view as? UICodeBlockWithDataInterface)?.block

        if (leftSide == removingViewBlock) {
            leftSide = null

            binding.leftCardText.apply {
                setText("")
                visibility = VISIBLE
            }
        }

        if (rightSide == removingViewBlock) {
            leftSide = null

            binding.rightCardText.apply {
                setText("")
                visibility = VISIBLE
            }
        }
    }

    private companion object {
        const val DRAG_AND_DROP_TAG = "EXPRESSION_BLOCK"
    }
}