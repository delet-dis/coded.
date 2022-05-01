package com.hits.coded.domain.extensions

import com.hits.coded.data.models.codeBlocks.bases.BlockBase

fun BlockBase.setIds(previousId: Int = -1): Int {
    var currentId = previousId + 1
    this.id = currentId

    this.nestedBlocks?.forEach {
        currentId = it.setIds(currentId)
    }

    return currentId
}