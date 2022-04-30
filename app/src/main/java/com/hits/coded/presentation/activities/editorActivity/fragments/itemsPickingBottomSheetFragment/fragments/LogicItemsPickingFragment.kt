package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hits.coded.data.models.itemsBottomSheet.interfaces.UIBottomSheetFragmentInterface
import com.hits.coded.databinding.FragmentLogicItemsPickingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogicItemsPickingFragment : Fragment(), UIBottomSheetFragmentInterface {
    private lateinit var binding: FragmentLogicItemsPickingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentLogicItemsPickingBinding.inflate(layoutInflater)

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

    override fun redrawElements() {

    }
}