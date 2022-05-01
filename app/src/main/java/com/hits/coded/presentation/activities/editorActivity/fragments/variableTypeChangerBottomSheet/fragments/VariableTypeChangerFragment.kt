package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.coded.data.interfaces.ui.bottomSheets.typeChangerBottomSheet.UIBottomSheetTypeChangerFragmentInterface
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.databinding.FragmentVariableTypeChangerBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments.recyclerViewAdapters.VariableTypeChangerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VariableTypeChangerFragment : Fragment(), UIBottomSheetTypeChangerFragmentInterface {
    private lateinit var binding: FragmentVariableTypeChangerBinding

    override var items: Array<VariableType> = ArrayList<VariableType>().toTypedArray()
        set(value) {
            field = value

            initRecyclerView()
        }
    override var onClickAction: (VariableType, Boolean) -> Unit = { _, _ -> }
        set(value) {
            field = value

            initRecyclerView()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentVariableTypeChangerBinding.inflate(layoutInflater)

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
        with(binding.variableTypeRecyclerView) {
            layoutManager = LinearLayoutManager(context)

            adapter =
                VariableTypeChangerAdapter(items, onClickAction, false)
        }
}