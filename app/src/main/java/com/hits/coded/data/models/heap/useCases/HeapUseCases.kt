package com.hits.coded.data.models.heap.useCases

import com.hits.coded.domain.useCases.heap.*

data class HeapUseCases(
    val addVariableUseCase: AddVariableUseCase,
    val getVariableUseCase: GetVariableUseCase,
    val deleteVariableUseCase: DeleteVariableUseCase,
    val reAssignVariableUseCase: ReAssignVariableUseCase,
    val isVariableDeclaredUseCase: IsVariableDeclaredUseCase
)