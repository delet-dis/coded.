package com.hits.coded.presentation.views.codeBlocks.variables

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockInterface
import com.hits.coded.data.models.uiCodeBlocks.interfaces.UICodeBlockWithDataInterface
import com.hits.coded.databinding.ViewVariableCreateBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIVariableCreationBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UICodeBlockInterface,
    UICodeBlockWithDataInterface {
    private val binding: ViewVariableCreateBlockBinding

    private var _block = VariableBlock(VariableBlockType.VARIABLE_CREATE)
    override val block: BlockBase
        get() = _block

    init {
        inflate(
            context,
            R.layout.view_variable_create_block,
            this
        ).also { view ->
            binding = ViewVariableCreateBlockBinding.bind(view)
        }

        this.initDragNDropGesture(this, DRAG_N_DROP_TAG)

        initVariableNameChangeListener()
    }

    private fun initVariableNameChangeListener() = binding.variableName.addTextChangedListener {
        _block.variableName = it?.toString()
    }

    private companion object {
        const val DRAG_N_DROP_TAG = "VARIABLE_CREATION_BLOCK_"
    }
}