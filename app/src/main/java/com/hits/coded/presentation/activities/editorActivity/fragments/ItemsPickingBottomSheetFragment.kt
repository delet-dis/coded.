package com.hits.coded.presentation.activities.editorActivity.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hits.coded.R
import com.hits.coded.databinding.FragmentItemsPickingBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsPickingBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentItemsPickingBottomSheetBinding

    private lateinit var behaviour: BottomSheetBehavior<View>

    private lateinit var parentLayout: View

    override fun getTheme() = R.style.bottomSheetFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentItemsPickingBottomSheetBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog

            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let {
                    parentLayout = it
                }

            parentLayout.let { parentLayoutUnwrapped ->
                behaviour = BottomSheetBehavior.from(parentLayoutUnwrapped)
            }

            setupFullHeight(parentLayout)
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        bottomSheet.layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    }
}