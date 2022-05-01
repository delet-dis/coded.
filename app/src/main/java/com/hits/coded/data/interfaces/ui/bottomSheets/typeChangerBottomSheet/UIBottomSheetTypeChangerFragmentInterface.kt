package com.hits.coded.data.interfaces.ui.bottomSheets.typeChangerBottomSheet

import com.hits.coded.data.models.sharedTypes.VariableType

interface UIBottomSheetTypeChangerFragmentInterface {
    var items: Array<VariableType>
    var onClickAction: (VariableType, Boolean) -> Unit

    fun initRecyclerView()
}