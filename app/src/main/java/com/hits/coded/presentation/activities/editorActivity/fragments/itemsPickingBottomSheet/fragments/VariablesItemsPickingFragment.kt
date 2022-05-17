package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.data.interfaces.callbacks.ui.UIEditorActivityShowBottomSheetCallback
import com.hits.coded.data.interfaces.ui.bottomSheets.itemsBottomSheet.UIBottomSheetItemsFragmentInterface
import com.hits.coded.data.models.codeBlocks.types.subBlocks.VariableBlockType
import com.hits.coded.databinding.FragmentVariablesItemsPickingBinding
import com.hits.coded.presentation.views.codeBlocks.variables.UIVariableChangeBlock
import com.hits.coded.presentation.views.codeBlocks.variables.UIVariableCreationBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VariablesItemsPickingFragment : Fragment(), UIBottomSheetItemsFragmentInterface {
    private lateinit var binding: FragmentVariablesItemsPickingBinding

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

    override fun redrawElements() =
        with(binding.variablesOperatorsLinearLayout) {
            removeAllViews()

            addView(UIVariableCreationBlock(requireContext()).apply {
                initCallback(requireActivity() as UIEditorActivityShowBottomSheetCallback)
            })

            addView(UIVariableChangeBlock(requireContext()).apply {
                blockType = VariableBlockType.VARIABLE_SET
            })
            addView(UIVariableChangeBlock(requireContext()).apply {
                blockType = VariableBlockType.VARIABLE_CHANGE
            })
        }
}