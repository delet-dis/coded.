package com.hits.coded.data.models.interpreterException

import com.hits.coded.data.models.sharedTypes.ExceptionType

class InterpreterException(
    val blockID: Int,
    val errorCode: ExceptionType,
    var msg: String = ""
) : Throwable()
