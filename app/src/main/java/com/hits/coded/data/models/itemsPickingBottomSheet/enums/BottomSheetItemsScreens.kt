package com.hits.coded.data.models.itemsPickingBottomSheet.enums

import com.hits.coded.R
import com.hits.coded.data.models.itemsPickingBottomSheet.dataClasses.BottomSheetItemsScreen
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments.ActionsItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments.LogicItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments.LoopsItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.fragments.VariablesItemsPickingFragment

enum class BottomSheetItemsScreens(val bottomSheetItemsScreen: BottomSheetItemsScreen) {
    VARIABLES(BottomSheetItemsScreen(VariablesItemsPickingFragment(), R.string.variables)),
    LOGIC(BottomSheetItemsScreen(LogicItemsPickingFragment(), R.string.logic)),
    LOOPS(BottomSheetItemsScreen(LoopsItemsPickingFragment(), R.string.loops)),
    ACTIONS(BottomSheetItemsScreen(ActionsItemsPickingFragment(), R.string.actions)),
}