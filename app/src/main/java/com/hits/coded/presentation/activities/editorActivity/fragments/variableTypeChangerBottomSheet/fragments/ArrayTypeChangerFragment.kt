package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.coded.data.interfaces.ui.bottomSheets.typeChangerBottomSheet.UIBottomSheetTypeChangerFragmentInterface
import com.hits.coded.data.models.types.VariableType
import com.hits.coded.databinding.FragmentArrayTypeChangerBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments.recyclerViewAdapters.VariableTypeChangerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArrayTypeChangerFragment : Fragment(), UIBottomSheetTypeChangerFragmentInterface {
    private lateinit var binding: FragmentArrayTypeChangerBinding

    override var items: Array<VariableType> = ArrayList<VariableType>().toTypedArray()
    override var onClickAction: (VariableType, Boolean) -> Unit = { _, _ -> }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentArrayTypeChangerBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            initRecyclerView()
        }
    }

    override fun initRecyclerView() =
        with(binding.arrayTypeRecyclerView) {
            layoutManager = LinearLayoutManager(context)

            adapter =
                VariableTypeChangerAdapter(items, onClickAction, true)
        }
}