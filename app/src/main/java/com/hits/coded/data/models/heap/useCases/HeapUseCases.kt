package com.hits.coded.data.models.heap.useCases

import com.hits.coded.domain.useCases.heap.AddVariableUseCase
import com.hits.coded.domain.useCases.heap.ClearUseCase
import com.hits.coded.domain.useCases.heap.DeleteVariableUseCase
import com.hits.coded.domain.useCases.heap.GetVariableUseCase
import com.hits.coded.domain.useCases.heap.IsVariableDeclaredUseCase
import com.hits.coded.domain.useCases.heap.ReAssignVariableUseCase

data class HeapUseCases(
    val addVariableUseCase: AddVariableUseCase,
    val getVariableUseCase: GetVariableUseCase,
    val deleteVariableUseCase: DeleteVariableUseCase,
    val reAssignVariableUseCase: ReAssignVariableUseCase,
    val isVariableDeclaredUseCase: IsVariableDeclaredUseCase,
    val clearUseCase: ClearUseCase
)