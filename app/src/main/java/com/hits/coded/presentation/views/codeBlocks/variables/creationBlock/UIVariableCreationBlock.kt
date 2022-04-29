package com.hits.coded.presentation.views.codeBlocks.variables.creationBlock

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hits.coded.R
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.data.models.heap.dataClasses.StoredVariable
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.databinding.ViewVariableCreateBlockBinding
import com.hits.coded.domain.extensions.findVariableType
import com.hits.coded.presentation.views.codeBlocks.variables.creationBlock.adapters.VariableTypeSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class UIVariableCreationBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation {
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

        initSpinnerTypes()

        initSpinnerListener()
    }

    private fun initSpinnerTypes() = with(binding.typeSpinner) {
        val adapter = VariableTypeSpinnerAdapter(
            VariableType.values().map { variableType ->
                variableType.name.lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.titlecase(Locale.getDefault())
                        } else {
                            it.toString()
                        }
                    }
            })

        binding.typeSpinner.adapter = adapter
    }

    private fun initSpinnerListener() = with(binding.typeSpinner) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                variableParams.type = p0?.let { findVariableType(it.selectedItem.toString()) }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
    }

    private fun initEditTextsDragAndDropGesture() =
        binding.variableName.setOnDragListener(null)

    private fun initVariableNameChangeListener() =
        binding.variableName.addTextChangedListener {
            variableParams.name = it.toString()
        }

    private companion object {
        const val DRAG_AND_DROP_TAG = "VARIABLE_CREATION_BLOCK_"
    }
}