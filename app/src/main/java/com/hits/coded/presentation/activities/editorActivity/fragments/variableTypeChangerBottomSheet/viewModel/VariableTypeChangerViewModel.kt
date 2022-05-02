package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.viewModel

import androidx.lifecycle.ViewModel
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.data.models.typeChangerBottomSheet.dataClasses.BottomSheetTypeChangerScreen
import com.hits.coded.data.models.typeChangerBottomSheet.enums.BottomSheetTypeChangersScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VariableTypeChangerViewModel @Inject constructor() : ViewModel() {
    fun getTypeChangerScreens(): Array<BottomSheetTypeChangerScreen> {
        val screens = ArrayList<BottomSheetTypeChangerScreen>()

        BottomSheetTypeChangersScreens.values().forEach {
            screens.add(it.bottomSheetTypeChangerScreen)
        }

        return screens.toTypedArray()
    }

    fun getVariablesTypes(): Array<VariableType> = VariableType.values()
}