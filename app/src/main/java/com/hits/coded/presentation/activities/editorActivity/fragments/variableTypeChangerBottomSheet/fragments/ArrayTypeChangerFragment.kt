package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.coded.data.interfaces.ui.typeChangerBottomSheet.UIBottomSheetTypeChangerFragmentInterface
import com.hits.coded.data.models.types.VariableType
import com.hits.coded.databinding.FragmentArrayTypeChangerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArrayTypeChangerFragment : Fragment(), UIBottomSheetTypeChangerFragmentInterface {
    private var binding: FragmentArrayTypeChangerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentArrayTypeChangerBinding.inflate(layoutInflater)

            binding!!.root
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

    private fun initRecyclerView() {
        binding!!.arrayTypeRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun initRecyclerView(
        context: Context,
        items: Array<VariableType>,
        onClickAction: (VariableType, Boolean) -> Unit
    ) {
        val test = binding

        binding!!.root.setBackgroundColor(Color.WHITE)

//        binding.arrayTypeRecyclerView.adapter =
//            VariableTypeChangerAdapter(items, onClickAction, true)
    }
}