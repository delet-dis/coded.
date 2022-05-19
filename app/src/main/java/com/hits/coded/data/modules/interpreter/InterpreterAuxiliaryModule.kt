package com.hits.coded.data.modules.interpreter

import com.hits.coded.data.implementations.repositories.interpreter.InterpreterAuxiliaryRepositoryImplementation
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterAuxiliaryUseCases
import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterAuxiliaryRepository
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.GetBaseTypeUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.GetVariableUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.InterpretAnyBlockUseCase
import com.hits.coded.domain.useCases.interpreter.auxiliaryFunctions.IsVariableUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterAuxiliaryModule {
    @Binds
    abstract fun bindInterpreterAuxiliaryRepository(
        auxiliaryFunctionsRepository: InterpreterAuxiliaryRepositoryImplementation
    ): InterpreterAuxiliaryRepository

    companion object {
        @Provides
        @Singleton
        fun provideInterpreterAuxiliaryUseCases(
            interpreterAuxiliaryRepository:
            InterpreterAuxiliaryRepository
        ): InterpreterAuxiliaryUseCases =
            InterpreterAuxiliaryUseCases(
                GetVariableUseCase(interpreterAuxiliaryRepository),
                IsVariableUseCase(interpreterAuxiliaryRepository),
                GetBaseTypeUseCase(interpreterAuxiliaryRepository),
                InterpretAnyBlockUseCase(interpreterAuxiliaryRepository)
            )
    }
}