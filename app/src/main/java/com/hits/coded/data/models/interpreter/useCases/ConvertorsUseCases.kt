package com.hits.coded.data.models.interpreter.useCases

import com.hits.coded.domain.useCases.interpreter.convertors.*

data class ConvertorsUseCases(
    val convertAnyToArrayBaseUseCase: ConvertAnyToArrayBaseUseCase,
    val convertAnyToBooleanUseCase: ConvertAnyToBooleanUseCase,
    val convertAnyToStringUseCase: ConvertAnyToStringUseCase,
    val convertAnyToStringIndulgentlyUseCase: ConvertAnyToStringIndulgentlyUseCase,
    val convertAnyToIntUseCase: ConvertAnyToIntUseCase,
    val convertAnyToDoubleUseCase: ConvertAnyToDoubleUseCase
)
