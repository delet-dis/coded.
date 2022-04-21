package com.hits.coded.data.models.heap.useCases

import com.hits.coded.domain.useCases.heap.AddVariableUseCase
import com.hits.coded.domain.useCases.heap.GetVariableUseCase
import com.hits.coded.domain.useCases.heap.ReAssignVariableUseCase

data class HeapUseCases(
    val addVariableUseCase: AddVariableUseCase,
    val getVariableUseCase: GetVariableUseCase,
    val reAssignVariableUseCase: ReAssignVariableUseCase
)