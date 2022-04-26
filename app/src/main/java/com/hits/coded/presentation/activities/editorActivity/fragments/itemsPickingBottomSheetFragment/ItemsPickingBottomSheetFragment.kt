package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hits.coded.R
import com.hits.coded.data.models.itemsBottomSheet.enums.BottomSheetItemsScreens
import com.hits.coded.data.models.itemsBottomSheet.interfaces.UIBottomSheetFragmentInterface
import com.hits.coded.data.models.uiSharedInterfaces.UIElementHandlesDragNDropInterface
import com.hits.coded.databinding.FragmentItemsPickingBottomSheetBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragmentStateAdapters.ItemsPickingViewPagerAdapter
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.viewModel.ItemsPickingBottomSheetFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsPickingBottomSheetFragment : BottomSheetDialogFragment(),
    UIElementHandlesDragNDropInterface {
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
            initDismissButtonOnClickListener()

            initDragNDropListener()
        }
    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(viewModel.getItemsScreens()[position].nameId).lowercase()
                .replaceFirstChar {
                    it.uppercase()
                }
        }.attach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog

            initViewPager()

            initTabLayoutMediator()

            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let {
                    parentLayout = it
                }

            parentLayout.let { parentLayoutUnwrapped ->
                behaviour = BottomSheetBehavior.from(parentLayoutUnwrapped)
            }

            setupFullHeight(parentLayout)
        }

        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        bottomSheet.layoutParams.height = requireActivity().window.decorView.height
    }

    private fun initViewPager() {
        binding.viewPager.adapter =
            ItemsPickingViewPagerAdapter(requireActivity(), viewModel.getItemsScreens())
    }

    private fun initDismissButtonOnClickListener() =
        binding.xMarkButton.setOnClickListener {
            dismiss()
        }

    override fun initDragNDropListener() {
        binding.viewPager.setOnDragListener { view, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_EXITED -> {
                    dismiss()

                    true
                }

                DragEvent.ACTION_DRAG_STARTED,
                DragEvent.ACTION_DRAG_LOCATION,
                DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_ENDED -> {
                    true
                }

                DragEvent.ACTION_DROP -> {
                    draggableItem.post { draggableItem.visibility = ConstraintLayout.VISIBLE }
                }

                else -> false
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        redrawCurrentViewPagerScreen()
    }

    private fun redrawCurrentViewPagerScreen() =
        (BottomSheetItemsScreens.values()[binding.viewPager.currentItem].bottomSheetItemsScreen.screen
                as? UIBottomSheetFragmentInterface)?.redrawElements()
}