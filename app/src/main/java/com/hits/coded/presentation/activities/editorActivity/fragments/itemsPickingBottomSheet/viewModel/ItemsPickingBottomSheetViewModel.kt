package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.viewModel

import androidx.lifecycle.ViewModel
import com.hits.coded.data.models.itemsBottomSheet.dataClasses.BottomSheetItemsScreen
import com.hits.coded.data.models.itemsBottomSheet.enums.BottomSheetItemsScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsPickingBottomSheetViewModel @Inject constructor() : ViewModel() {
    fun getItemsScreens(): Array<BottomSheetItemsScreen> {
        val itemsScreens = ArrayList<BottomSheetItemsScreen>()

        BottomSheetItemsScreens.values().forEach {
            itemsScreens.add(it.bottomSheetItemsScreen)
        }

        return itemsScreens.toTypedArray()
    }
}