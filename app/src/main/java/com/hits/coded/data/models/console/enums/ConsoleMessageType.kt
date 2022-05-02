package com.hits.coded.data.models.console.enums

import com.hits.coded.R

enum class ConsoleMessageType(val colorResourceId: Int) {
    ERROR(R.color.lightRed),
    OUTPUT(R.color.lightGreen),
    INPUT(R.color.lightBluePurple)
}