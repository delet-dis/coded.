package com.hits.coded.data.interfaces.callbacks.ui

import com.hits.coded.data.models.types.VariableType

interface UIEditorActivityShowBottomSheetCallback {
    fun showTypeChangingBottomSheet(closureToInvoke: (VariableType, Boolean) -> Unit)
}