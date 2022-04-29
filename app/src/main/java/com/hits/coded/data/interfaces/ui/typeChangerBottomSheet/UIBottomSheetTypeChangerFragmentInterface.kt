package com.hits.coded.data.interfaces.ui.typeChangerBottomSheet

import android.content.Context
import com.hits.coded.data.models.types.VariableType

interface UIBottomSheetTypeChangerFragmentInterface {
    fun initRecyclerView(
        context: Context,
        items: Array<VariableType>,
        onClickAction: (VariableType, Boolean) -> Unit
    )
}