package com.hits.coded.data.models.typeChangerBottomSheet.enums

import com.hits.coded.R
import com.hits.coded.data.models.itemsPickingBottomSheet.dataClasses.BottomSheetItemsScreen
import com.hits.coded.data.models.typeChangerBottomSheet.dataClasses.BottomSheetTypeChangerScreen
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments.ArrayTypeChangerFragment
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments.VariableTypeChangerFragment

enum class BottomSheetTypeChangersScreens(val bottomSheetTypeChangerScreen: BottomSheetTypeChangerScreen) {
    VARIABLE_TYPE(
        BottomSheetTypeChangerScreen(
            VariableTypeChangerFragment(),
            R.string.variableType
        )
    ),
    ARRAY_TYPE(
        BottomSheetTypeChangerScreen(
            ArrayTypeChangerFragment(),
            R.string.arrayType
        )
    )
}