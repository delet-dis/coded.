package com.hits.coded.data.modules

import com.hits.coded.data.models.interpreterCaller.useCases.InterpreterCallerUseCases
import com.hits.coded.data.repositoriesImplementations.InterpreterCallerRepositoryImplementation
import com.hits.coded.domain.repositories.InterpreterCallerRepository
import com.hits.coded.domain.useCases.interpreterCaller.CallInterpreterUseCase
import com.hits.coded.domain.useCases.interpreterCaller.GetExecutionResultsUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterCallerModule {
    @Binds
    abstract fun bindInterpreterCallerRepository(
        interpreterCallerRepository: InterpreterCallerRepositoryImplementation
    ): InterpreterCallerRepository

    companion object {
        @Provides
        @Singleton
        fun provideInterpreterCallerUseCases(
            interpreterCallerRepository: InterpreterCallerRepository
        ): InterpreterCallerUseCases =
            InterpreterCallerUseCases(
                CallInterpreterUseCase(interpreterCallerRepository),
                GetExecutionResultsUseCase(interpreterCallerRepository)
            )
    }
}