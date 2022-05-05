package com.hits.coded.presentation.views.codeBlocks.variables

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.interfaces.callbacks.ui.UIEditorActivityShowBottomSheetCallback
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.databinding.ViewVariableCreateBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIVariableCreationBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation,
    UICodeBlockSupportsErrorDisplaying {
    private val binding: ViewVariableCreateBlockBinding

    private var variableParams = StoredVariable()

    private var _block = VariableBlock(VariableBlockType.VARIABLE_CREATE, variableParams)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    init {
        inflate(
            context,
            R.layout.view_variable_create_block,
            this
        ).also { view ->
            binding = ViewVariableCreateBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)

        initVariableNameChangeListener()

        initEditTextsDragAndDropGesture()
    }

    private fun initEditTextsDragAndDropGesture() =
        binding.variableName.setOnDragListener(null)

    private fun initVariableNameChangeListener() =
        binding.variableName.addTextChangedListener {
            variableParams.name = it.toString()
        }

    @SuppressLint("SetTextI18n")
    fun initCallback(callback: UIEditorActivityShowBottomSheetCallback) =
        with(binding.variableType) {
            setOnClickListener {
                callback.showTypeChangingBottomSheet { type, isArray ->
                    variableParams.type = type
                    variableParams.isArray = isArray

                    text = this.context.getString(type.typeAsStringResource) + " " +
                            if (isArray) this.context.getString(R.string.array) else ""

                    callback.hideTypeChangerBottomSheet()
                }
            }
        }

    override fun displayError() =
        binding.backgroundImage.setImageResource(R.drawable.variable_block)

    override fun hideError() =
        binding.backgroundImage.setImageResource(R.drawable.error_block)

    private companion object {
        const val DRAG_AND_DROP_TAG = "VARIABLE_CREATION_BLOCK_"
    }
}