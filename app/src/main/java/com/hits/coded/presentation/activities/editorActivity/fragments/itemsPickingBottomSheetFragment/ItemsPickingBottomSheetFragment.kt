package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hits.coded.R
import com.hits.coded.data.models.itemsBottomSheet.ItemsScreens
import com.hits.coded.databinding.FragmentItemsPickingBottomSheetBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragmentStateAdapters.ItemsPickingViewPagerAdapter
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.viewModel.ItemsPickingBottomSheetFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsPickingBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentItemsPickingBottomSheetBinding

    private val viewModel: ItemsPickingBottomSheetFragmentViewModel by viewModels()

    private lateinit var behaviour: BottomSheetBehavior<View>

    private lateinit var parentLayout: View

    override fun getTheme() = R.style.bottomSheetFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentItemsPickingBottomSheetBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            initViewPager()

            initTabLayoutMediator()

            initDismissButtonOnClickListener()
        }
    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = ItemsScreens.values()[position].name.lowercase().replaceFirstChar {
                it.uppercase()
            }
        }.attach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog

            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let {
                    parentLayout = it
                }

            parentLayout.let { parentLayoutUnwrapped ->
                behaviour = BottomSheetBehavior.from(parentLayoutUnwrapped)
            }

            setupFullHeight(parentLayout)
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED

            initHalfExpandedHiding()
        }

        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        bottomSheet.layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    }

    private fun initViewPager() {
        binding.viewPager.adapter =
            ItemsPickingViewPagerAdapter(requireActivity(), viewModel.getItemsScreens())
    }

    private fun initHalfExpandedHiding() =
        behaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    behaviour.state = BottomSheetBehavior.STATE_COLLAPSED

                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    private fun initDismissButtonOnClickListener() =
        binding.xMarkButton.setOnClickListener {
            dismiss()
        }

    override fun dismiss() {
        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED

        super.dismiss()
    }
}