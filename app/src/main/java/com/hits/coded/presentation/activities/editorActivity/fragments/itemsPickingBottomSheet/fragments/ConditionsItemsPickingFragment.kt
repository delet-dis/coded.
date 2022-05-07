package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.data.interfaces.ui.bottomSheets.itemsBottomSheet.UIBottomSheetItemsFragmentInterface
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.LogicalBlockType
import com.hits.coded.data.models.codeBlocks.types.subBlocks.condition.subBlocks.MathematicalBlockType
import com.hits.coded.databinding.FragmentConditionsItemsPickingBinding
import com.hits.coded.presentation.views.codeBlocks.conditions.UIConditionBlock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConditionsItemsPickingFragment : Fragment(), UIBottomSheetItemsFragmentInterface {
    private lateinit var binding: FragmentConditionsItemsPickingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentConditionsItemsPickingBinding.inflate(layoutInflater)

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
        binding.mathematicalOperatorsLinearLayout.apply {
            removeAllViews()

            MathematicalBlockType.values().forEach {
                addView(UIConditionBlock(requireContext()).apply {
                    mathematicalBlockType = it
                })
            }
        }

        binding.logicalOperatorsLinearLayout.apply {
            removeAllViews()

            LogicalBlockType.values().forEach {
                addView(UIConditionBlock(requireContext()).apply {
                    logicalBlockType = it
                })
            }
        }
    }
}