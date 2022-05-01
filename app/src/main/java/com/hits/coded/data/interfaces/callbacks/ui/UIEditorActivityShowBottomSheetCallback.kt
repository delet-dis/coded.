package com.hits.coded.data.interfaces.callbacks.ui

import com.hits.coded.data.models.sharedTypes.VariableType

interface UIEditorActivityShowBottomSheetCallback {
    fun showTypeChangingBottomSheet(closureToInvoke: (VariableType, Boolean) -> Unit)
    fun hideTypeChangerBottomSheet()
}