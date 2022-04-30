package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet

import android.app.Activity
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.hits.coded.data.interfaces.ui.UIElementHandlesDragAndDropInterface
import com.hits.coded.data.interfaces.ui.bottomSheets.UIBottomSheetInterface
import com.hits.coded.data.interfaces.ui.bottomSheets.UIBottomSheetWithViewPagerInterface
import com.hits.coded.data.interfaces.ui.bottomSheets.itemsBottomSheet.UIBottomSheetItemsFragmentInterface
import com.hits.coded.data.models.itemsPickingBottomSheet.enums.BottomSheetItemsScreens
import com.hits.coded.databinding.IncludeItemsPickingBottomSheetBinding
import com.hits.coded.domain.extensions.dpToPx
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragmentStateAdapters.ItemsPickingViewPagerAdapter
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.viewModel.ItemsPickingBottomSheetViewModel


class ItemsPickingBottomSheetController(
    private val binding: IncludeItemsPickingBottomSheetBinding,
    private val viewModel: ItemsPickingBottomSheetViewModel,
    private val parentActivity: Activity
) : UIElementHandlesDragAndDropInterface, UIBottomSheetWithViewPagerInterface, UIBottomSheetInterface {

    private val behaviour = BottomSheetBehavior.from(binding.itemsPickingBottomSheetLayout)

    init {
        initDismissButtonOnClickListener()

        initDragAndDropListener()

        initViewPager()

        initTabLayoutMediator()
    }

    override fun initTabLayoutMediator() =
        TabLayoutMediator(
            binding.itemsPickingTabLayout,
            binding.itemsPickingViewPager
        ) { tab, position ->
            tab.text = binding.root.context.getString(viewModel.getItemsScreens()[position].nameId)
                .lowercase()
                .replaceFirstChar {
                    it.uppercase()
                }
        }.attach()

    override fun show() {
        behaviour.isFitToContents = false
        behaviour.expandedOffset = 50.dpToPx(binding.root.context)

        behaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        redrawCurrentViewPagerScreen()
    }

    override fun initViewPager() {
        binding.itemsPickingViewPager.adapter =
            ItemsPickingViewPagerAdapter(
                parentActivity as FragmentActivity,
                viewModel.getItemsScreens()
            )
    }

    override fun initDismissButtonOnClickListener() =
        binding.itemsPickingxMarkButton.setOnClickListener {
            behaviour.state = BottomSheetBehavior.STATE_HIDDEN
        }

    override fun initDragAndDropListener() =
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

    private fun redrawCurrentViewPagerScreen() =
        (BottomSheetItemsScreens.values()[binding.itemsPickingViewPager.currentItem].bottomSheetItemsScreen.screen
                as? UIBottomSheetItemsFragmentInterface)?.redrawElements()
}