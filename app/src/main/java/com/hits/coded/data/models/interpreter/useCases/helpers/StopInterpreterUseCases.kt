package com.hits.coded.data.models.interpreter.useCases.helpers

import com.hits.coded.domain.useCases.interpreter.helpers.StartInterpreterUseCase
import com.hits.coded.domain.useCases.interpreter.helpers.StopInterpreterUseCase

data class StopInterpreterUseCases(
    val stopInterpreterUseCase: StopInterpreterUseCase,
    val startInterpreterUseCase: StartInterpreterUseCase
)
