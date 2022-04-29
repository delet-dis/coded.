package com.hits.coded.data.models.interpreterCaller.useCases

import com.hits.coded.domain.useCases.interpreterCaller.CallInterpreterUseCase
import com.hits.coded.domain.useCases.interpreterCaller.GetExecutionResultsUseCase

data class InterpreterCallerUseCases(
    val callInterpreterUseCase: CallInterpreterUseCase,
    val getExecutionResultsUseCase: GetExecutionResultsUseCase
)