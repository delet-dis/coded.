package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.viewModel

import androidx.lifecycle.ViewModel
import com.hits.coded.data.models.itemsBottomSheet.ItemsScreen
import com.hits.coded.data.models.itemsBottomSheet.ItemsScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsPickingBottomSheetFragmentViewModel @Inject constructor() : ViewModel() {
    fun getItemsScreens(): Array<ItemsScreen> {
        val itemsScreens = ArrayList<ItemsScreen>()

        ItemsScreens.values().forEach {
            itemsScreens.add(it.itemsScreen)
        }

        return itemsScreens.toTypedArray()
    }
}