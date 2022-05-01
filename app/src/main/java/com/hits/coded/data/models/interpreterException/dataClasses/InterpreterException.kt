package com.hits.coded.data.models.interpreterException.dataClasses

import com.hits.coded.data.models.sharedTypes.ExceptionType

data class InterpreterException(
    val blockID: Int,
    val errorCode: ExceptionType,
    var msg: String = ""
) : Throwable()
