package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.data.interfaces.ui.bottomSheets.itemsBottomSheet.UIBottomSheetItemsFragmentInterface
import com.hits.coded.databinding.FragmentActionsItemsPickingBinding
import com.hits.coded.presentation.views.codeBlocks.console.UIConsoleReadBlock
import com.hits.coded.presentation.views.codeBlocks.console.UIConsoleWriteBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActionsItemsPickingFragment : Fragment(), UIBottomSheetItemsFragmentInterface {
    private lateinit var binding: FragmentActionsItemsPickingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentActionsItemsPickingBinding.inflate(layoutInflater)

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
        with(binding.actionsLinearLayout) {
            removeAllViews()

            with(requireContext()) {
                addView(getBlockInHorizontalScrollView(UIConsoleWriteBlock(this)))
                addView(getBlockInHorizontalScrollView(UIConsoleReadBlock(this)))
            }
        }
    }
}