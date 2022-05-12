package com.hits.coded.data.models.interpreterException.dataClasses

import com.hits.coded.data.models.sharedTypes.ExceptionType

data class InterpreterException(
    val errorCode: ExceptionType,
    var blockID: Int = 0,
    var msg: String = ""
) : Throwable()
