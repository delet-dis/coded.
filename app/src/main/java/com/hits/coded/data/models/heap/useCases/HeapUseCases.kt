package com.hits.coded.data.models.heap.useCases

import com.hits.coded.domain.useCases.heap.AddVariableUseCase
import com.hits.coded.domain.useCases.heap.FindVariableUseCase
import com.hits.coded.domain.useCases.heap.ReAssignUseCase

data class HeapUseCases (
    val addVariableUseCase:AddVariableUseCase,
    val findVariableUseCase: FindVariableUseCase,
    val reAssignUseCase: ReAssignUseCase
)