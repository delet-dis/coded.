package com.hits.coded.data.models.itemsBottomSheet.enums

import com.hits.coded.R
import com.hits.coded.data.models.itemsBottomSheet.dataClasses.BottomSheetItemsScreen
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.ActionsItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.LogicItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.LoopsItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment.VariablesItemsPickingFragment

enum class BottomSheetItemsScreens(val bottomSheetItemsScreen: BottomSheetItemsScreen) {
    VARIABLES(BottomSheetItemsScreen(VariablesItemsPickingFragment(), R.string.variables)),
    LOGIC(BottomSheetItemsScreen(LogicItemsPickingFragment(), R.string.logic)),
    LOOPS(BottomSheetItemsScreen(LoopsItemsPickingFragment(), R.string.loops)),
    ACTIONS(BottomSheetItemsScreen(ActionsItemsPickingFragment(), R.string.actions)),
}