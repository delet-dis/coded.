package com.hits.coded.presentation.activities.editorActivity.fragments.consoleBottomSheet

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hits.coded.data.interfaces.ui.bottomSheets.UIBottomSheetInterface
import com.hits.coded.databinding.IncludeConsoleBottomSheetBinding
import com.hits.coded.domain.extensions.dpToPx

class ConsoleBottomSheetController(private val binding: IncludeConsoleBottomSheetBinding) :
    UIBottomSheetInterface {
    private val behaviour = BottomSheetBehavior.from(binding.bottomSheetLayout)

    init {
        initDismissButtonOnClickListener()
    }

    override fun initDismissButtonOnClickListener() =
        binding.xMarkButton.setOnClickListener {
            behaviour.state = BottomSheetBehavior.STATE_HIDDEN
        }

    override fun show() {
        behaviour.isFitToContents = false
        behaviour.expandedOffset = 50.dpToPx(binding.root.context)

        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun show(navigationBarHeight: Int) {
        show()

        binding.console.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(
                leftMargin,
                topMargin,
                rightMargin,
                (navigationBarHeight + 60.dpToPx(binding.root.context))
            )
        }
    }
}