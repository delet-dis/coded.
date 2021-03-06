package com.hits.coded.data.modules

import com.hits.coded.data.implementations.repositories.HeapRepositoryImplementation
import com.hits.coded.data.models.heap.useCases.HeapUseCases
import com.hits.coded.domain.repositories.HeapRepository
import com.hits.coded.domain.useCases.heap.AddVariableUseCase
import com.hits.coded.domain.useCases.heap.ClearUseCase
import com.hits.coded.domain.useCases.heap.DeleteVariableUseCase
import com.hits.coded.domain.useCases.heap.GetVariableUseCase
import com.hits.coded.domain.useCases.heap.IsVariableDeclaredUseCase
import com.hits.coded.domain.useCases.heap.ReAssignVariableUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HeapModule {
    @Binds
    abstract fun bindHeapRepository(heapRepository: HeapRepositoryImplementation): HeapRepository

    companion object {
        @Provides
        @Singleton
        fun provideHeapUseCases(heapRepository: HeapRepository): HeapUseCases = HeapUseCases(
            AddVariableUseCase(heapRepository),
            GetVariableUseCase(heapRepository),
            DeleteVariableUseCase(heapRepository),
            ReAssignVariableUseCase(heapRepository),
            IsVariableDeclaredUseCase(heapRepository),
            ClearUseCase(heapRepository)
        )
    }
}