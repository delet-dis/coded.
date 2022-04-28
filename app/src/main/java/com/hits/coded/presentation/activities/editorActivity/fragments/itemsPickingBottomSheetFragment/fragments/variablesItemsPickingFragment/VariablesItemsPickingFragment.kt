package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hits.coded.data.models.itemsBottomSheet.interfaces.UIBottomSheetFragmentInterface
import com.hits.coded.databinding.FragmentVariablesItemsPickingBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment.viewModels.VariablesItemsPickingFragmentViewModel
import com.hits.coded.presentation.views.codeBlocks.variables.UIVariableChangeByBlock
import com.hits.coded.presentation.views.codeBlocks.variables.creationBlock.UIVariableCreationBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VariablesItemsPickingFragment : Fragment(), UIBottomSheetFragmentInterface {
    private lateinit var binding: FragmentVariablesItemsPickingBinding

    private val viewModel: VariablesItemsPickingFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentVariablesItemsPickingBinding.inflate(layoutInflater)

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
        binding.variablesActionsLinearLayout.apply {
            removeAllViews()
            addView(UIVariableCreationBlock(requireContext()))
            addView(UIVariableChangeByBlock(requireContext()))
        }
    }
}