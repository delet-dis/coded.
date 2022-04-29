package com.hits.coded.data.modules

import com.hits.coded.data.models.interpreter.useCases.InterpreterUseCases
import com.hits.coded.data.repositoriesImplementations.InterpreterRepositoryImplementation
import com.hits.coded.domain.repositories.InterpreterRepository
import com.hits.coded.domain.useCases.interpreter.InterpretConditionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretExpressionBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretIOBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretLoopBlockUseCase
import com.hits.coded.domain.useCases.interpreter.InterpretVariableBlockUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpretatorModule {
    @Binds
    abstract fun bindInterpretatorRepository(interpretatorRepository: InterpreterRepositoryImplementation): InterpreterRepository

    companion object {
        @Provides
        @Singleton
        fun provideInterpreterUseCases(interpreterRepository: InterpreterRepository): InterpreterUseCases =
            InterpreterUseCases(
                InterpretConditionBlockUseCase(interpreterRepository),
                InterpretExpressionBlockUseCase(interpreterRepository),
                InterpretIOBlockUseCase(interpreterRepository),
                InterpretLoopBlockUseCase(interpreterRepository),
                InterpretVariableBlockUseCase(interpreterRepository)
            )
    }
}