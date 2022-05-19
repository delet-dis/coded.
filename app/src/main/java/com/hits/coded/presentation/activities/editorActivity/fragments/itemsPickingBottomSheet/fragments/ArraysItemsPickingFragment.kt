package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.data.interfaces.ui.bottomSheets.itemsBottomSheet.UIBottomSheetItemsFragmentInterface
import com.hits.coded.databinding.FragmentArraysItemsPickingBinding
import com.hits.coded.presentation.views.codeBlocks.arrays.UIArrayAddBlock
import com.hits.coded.presentation.views.codeBlocks.arrays.UIArrayGetBlock
import com.hits.coded.presentation.views.codeBlocks.arrays.UIArrayGetLengthBlock

class ArraysItemsPickingFragment : Fragment(), UIBottomSheetItemsFragmentInterface {
    private lateinit var binding: FragmentArraysItemsPickingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentArraysItemsPickingBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            redrawElements()
        }
    }

    override fun redrawElements() {
        with(binding.arraysOperatorsLinearLayout) {
            removeAllViews()

            with(requireContext()) {
                addView(UIArrayAddBlock(this))

                addView(UIArrayGetBlock(this))

                addView(UIArrayGetLengthBlock(this))
            }
        }
    }
}