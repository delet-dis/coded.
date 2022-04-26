package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragmentStateAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hits.coded.data.models.itemsBottomSheet.dataClasses.BottomSheetItemsScreen


class ItemsPickingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val items: Array<BottomSheetItemsScreen>
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int =
        items.size

    override fun createFragment(position: Int): Fragment =
        items[position].screen
}