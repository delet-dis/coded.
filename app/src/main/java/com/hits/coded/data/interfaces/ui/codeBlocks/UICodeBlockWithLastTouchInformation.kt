package com.hits.coded.data.interfaces.ui.codeBlocks

interface UICodeBlockWithLastTouchInformation {
    var touchX: Int
    var touchY: Int

    fun setLastTouchInformation(touchX: Int, touchY: Int){
        this.touchX = touchX
        this.touchY = touchY
    }
}