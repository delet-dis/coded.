package com.hits.coded.data.models.uiCodeBlocks.interfaces

interface UICodeBlockWithLastTouchInformation {
    var touchX: Int
    var touchY: Int

    fun setLastTouchInformation(touchX: Int, touchY: Int){
        this.touchX = touchX
        this.touchY = touchY
    }
}