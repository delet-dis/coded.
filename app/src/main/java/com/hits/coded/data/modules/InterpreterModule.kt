package com.hits.coded.data.modules

import com.hits.coded.data.models.interpreter.useCases.InterpreterUseCases
import com.hits.coded.data.implementations.repositories.InterpreterRepositoryImplementation
import com.hits.coded.domain.repositories.InterpreterRepository
import com.hits.coded.domain.useCases.interpreter.GetCurrentBlockIdUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretStartBlockUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterModule {
    @Binds
    abstract fun bindInterpreterRepository(interpreterRepository: InterpreterRepositoryImplementation): InterpreterRepository

    companion object {
        @Provides
        @Singleton
        fun provideInterpreterUseCases(interpreterRepository: InterpreterRepository): InterpreterUseCases =
            InterpreterUseCases(
                InterpretStartBlockUseCase(interpreterRepository),
                GetCurrentBlockIdUseCase(interpreterRepository)
            )
    }
}