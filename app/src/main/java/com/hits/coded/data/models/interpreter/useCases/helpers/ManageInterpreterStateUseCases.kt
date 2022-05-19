package com.hits.coded.data.models.interpreter.useCases.helpers

import com.hits.coded.domain.useCases.interpreter.helpers.StartInterpreterUseCase
import com.hits.coded.domain.useCases.interpreter.helpers.StopInterpreterUseCase

data class ManageInterpreterStateUseCases(
    val stopInterpreterUseCase: StopInterpreterUseCase,
    val startInterpreterUseCase: StartInterpreterUseCase
)
