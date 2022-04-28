package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet

import android.annotation.SuppressLint
import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hits.coded.databinding.IncludeVariableTypeChangerBottomSheetBinding

class VariableTypeChangerBottomSheetController(
    private val binding: IncludeVariableTypeChangerBottomSheetBinding,
    private val parentActivity: Activity
) {

    private val behaviour = BottomSheetBehavior.from(binding.typeChangerBottomSheetLayout)

    init {
        initParentViewOnTouchListener()

        initDismissButtonOnClickListener()
    }

    fun show() {
        behaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initParentViewOnTouchListener() {
        binding.typeChangerBottomSheetLayout.setOnTouchListener { _, _ ->
            true
        }
    }

    private fun initDismissButtonOnClickListener() =
        binding.typeChangerxMarkButton.setOnClickListener {
            behaviour.state = BottomSheetBehavior.STATE_HIDDEN
        }


}
