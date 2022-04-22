package com.hits.coded.data.modules

import com.hits.coded.data.models.interpreter.InterpreterUseCases
import com.hits.coded.data.repositoriesImplementations.InterpreterRepositoryImplementation
import com.hits.coded.domain.repositories.InterpreterRepository
import com.hits.coded.domain.useCases.interpreter.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpretatorModule {
    abstract fun bindInterpretatorRepository(interpretatorRepository: InterpreterRepositoryImplementation): InterpreterRepository

    companion object {
        @Provides
        @Singleton
        fun provideInterpreterUseCases(interpreterRepository: InterpreterRepository): InterpreterUseCases =
            InterpreterUseCases(
                InterpreteConditionBlockUseCase(interpreterRepository),
                InterpreteExpressionBlockUseCase(interpreterRepository),
                InterpreteIOBlockUseCase(interpreterRepository),
                InterpreteLoopBlockUseCase(interpreterRepository),
                InterpreteVariableBlockUseCase(interpreterRepository)
            )
    }
}