package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.data.models.itemsBottomSheet.interfaces.UIBottomSheetFragmentInterface
import com.hits.coded.databinding.FragmentActionsItemsPickingBinding
import com.hits.coded.presentation.views.codeBlocks.actions.console.UIActionConsoleWriteBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActionsItemsPickingFragment : Fragment(), UIBottomSheetFragmentInterface {
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
        binding.actionsLinearLayout.apply {
            removeAllViews()
            addView(UIActionConsoleWriteBlock(requireContext()))
        }
    }
}