package com.hits.coded.data.models.interpreter.useCases.repositories

import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToArrayBaseUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToBooleanUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToDoubleUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToIntUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToStringIndulgentlyUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToStringUseCase

data class InterpreterConverterUseCases(
    val convertAnyToArrayBaseUseCase: ConvertAnyToArrayBaseUseCase,
    val convertAnyToBooleanUseCase: ConvertAnyToBooleanUseCase,
    val convertAnyToStringUseCase: ConvertAnyToStringUseCase,
    val convertAnyToStringIndulgentlyUseCase: ConvertAnyToStringIndulgentlyUseCase,
    val convertAnyToIntUseCase: ConvertAnyToIntUseCase,
    val convertAnyToDoubleUseCase: ConvertAnyToDoubleUseCase
)
