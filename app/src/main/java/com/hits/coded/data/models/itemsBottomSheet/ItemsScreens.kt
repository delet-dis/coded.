package com.hits.coded.data.models.itemsBottomSheet

import com.hits.coded.R
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.ActionsItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.LogicItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.LoopsItemsPickingFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment.VariablesItemsPickingFragment

enum class ItemsScreens(val itemsScreen: ItemsScreen) {
    VARIABLES(ItemsScreen(VariablesItemsPickingFragment(), R.string.variables)),
    LOGIC(ItemsScreen(LogicItemsPickingFragment(), R.string.logic)),
    LOOPS(ItemsScreen(LoopsItemsPickingFragment(), R.string.loops)),
    ACTIONS(ItemsScreen(ActionsItemsPickingFragment(), R.string.actions)),
}