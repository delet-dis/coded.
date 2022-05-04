package com.hits.coded.presentation.views.codeBlocks.expressions

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.UIElementHandlesCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSavesNestedBlocksInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.ExpressionBlock
import com.hits.coded.databinding.ViewExpressionBlockBinding

//class UIExpressionBlock @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
//    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
//    UIElementHandlesDragAndDropInterface, UICodeBlockElementHandlesDragAndDropInterface,
//    UICodeBlockWithCustomRemoveViewProcessInterface,
//    UIElementHandlesCustomRemoveViewProcessInterface, UICodeBlockSavesNestedBlocksInterface {
//    private val binding: ViewExpressionBlockBinding
//
//    override val nestedUIBlocks: ArrayList<View> = ArrayList()
//
//    private var leftSide = Any()
//    private var rightSide = Any()
//
//    private var _block = ExpressionBlock()
//    override val block: BlockBase
//        get() = _block
//
//    init {
//        inflate(
//            context,
//            R.layout.view_expression_block,
//            this
//        ).also { view ->
//            binding = ViewExpressionBlockBinding.bind(view)
//        }
//    }
//}