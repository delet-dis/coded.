package com.hits.coded.presentation.views.codeBlocks.console

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockSupportsErrorDisplaying
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithDataInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockWithLastTouchInformation
import com.hits.coded.data.interfaces.ui.codeBlocks.UIMoveableCodeBlockInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UINestedableCodeBlock
import com.hits.coded.data.models.codeBlocks.bases.BlockBase
import com.hits.coded.data.models.codeBlocks.dataClasses.IOBlock
import com.hits.coded.data.models.codeBlocks.types.subBlocks.IOBlockType
import com.hits.coded.databinding.ViewConsoleReadBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UIConsoleReadBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UIMoveableCodeBlockInterface,
    UICodeBlockWithDataInterface, UICodeBlockWithLastTouchInformation, UINestedableCodeBlock,
    UICodeBlockSupportsErrorDisplaying {
    private val binding: ViewConsoleReadBlockBinding

    private var _block = IOBlock(IOBlockType.READ)
    override val block: BlockBase
        get() = _block

    override var touchX: Int = 0
    override var touchY: Int = 0

    init {
        inflate(
            context,
            R.layout.view_console_read_block,
            this
        ).also { view ->
            binding = ViewConsoleReadBlockBinding.bind(view)
        }

        this.initDragAndDropGesture(this, DRAG_AND_DROP_TAG)
    }

    override fun displayError() =
        binding.backgroundImage.setImageResource(R.drawable.error_small_block)

    override fun hideError() =
        binding.backgroundImage.setImageResource(R.drawable.console_read_block)

    private companion object {
        const val DRAG_AND_DROP_TAG = "ACTION_CONSOLE_READ_BLOCK_"
    }
}