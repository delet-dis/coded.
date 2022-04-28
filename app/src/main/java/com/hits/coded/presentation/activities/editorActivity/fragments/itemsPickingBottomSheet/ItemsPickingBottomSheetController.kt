package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet

import android.annotation.SuppressLint
import android.app.Activity
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.hits.coded.data.models.itemsBottomSheet.enums.BottomSheetItemsScreens
import com.hits.coded.data.models.itemsBottomSheet.interfaces.UIBottomSheetFragmentInterface
import com.hits.coded.data.models.uiSharedInterfaces.UIElementHandlesDragAndDropInterface
import com.hits.coded.databinding.IncludeItemsPickingBottomSheetBinding
import com.hits.coded.domain.extensions.dpToPx
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragmentStateAdapters.ItemsPickingViewPagerAdapter
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.viewModel.ItemsPickingBottomSheetViewModel


class ItemsPickingBottomSheetController(
    private val binding: IncludeItemsPickingBottomSheetBinding,
    private val viewModel: ItemsPickingBottomSheetViewModel,
    private val parentActivity: Activity
) : UIElementHandlesDragAndDropInterface {

    private val behaviour = BottomSheetBehavior.from(binding.itemsPickingBottomSheetLayout)

    init {
        initParentViewOnTouchListener()

        initDismissButtonOnClickListener()

        initDragAndDropListener()

        initViewPager()

        initTabLayoutMediator()

        initTopMargin()
    }

    private fun initTopMargin() {
        binding.itemsPickingBottomSheetLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 50.dpToPx(binding.root.context), 0, 0)
        }
    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(binding.itemsPickingTabLayout, binding.itemsPickingViewPager) { tab, position ->
            tab.text = binding.root.context.getString(viewModel.getItemsScreens()[position].nameId)
                .lowercase()
                .replaceFirstChar {
                    it.uppercase()
                }
        }.attach()
    }

    fun show() {
        behaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        redrawCurrentViewPagerScreen()
    }

    private fun initViewPager() {
        binding.itemsPickingViewPager.adapter =
            ItemsPickingViewPagerAdapter(
                parentActivity as FragmentActivity,
                viewModel.getItemsScreens()
            )
    }

    private fun initDismissButtonOnClickListener() =
        binding.itemsPickingxMarkButton.setOnClickListener {
            behaviour.state = BottomSheetBehavior.STATE_HIDDEN
        }

    override fun initDragAndDropListener() {
        binding.itemsPickingViewPager.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    behaviour.state = BottomSheetBehavior.STATE_HIDDEN

                    true
                }

                DragEvent.ACTION_DRAG_EXITED,
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initParentViewOnTouchListener() {
        binding.itemsPickingBottomSheetLayout.setOnTouchListener { _, _ ->
            true
        }
    }

    private fun redrawCurrentViewPagerScreen() =
        (BottomSheetItemsScreens.values()[binding.itemsPickingViewPager.currentItem].bottomSheetItemsScreen.screen
                as? UIBottomSheetFragmentInterface)?.redrawElements()
}