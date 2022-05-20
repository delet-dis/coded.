package com.hits.coded.data.modules.interpreter

import com.hits.coded.data.implementations.helpers.InterpreterManagerImplementation
import com.hits.coded.data.models.interpreter.useCases.helpers.ManageInterpreterStateUseCases
import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterManager
import com.hits.coded.domain.useCases.interpreter.helpers.StartInterpreterUseCase
import com.hits.coded.domain.useCases.interpreter.helpers.StopInterpreterUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterManagerModule {
    @Binds
    abstract fun bindInterpreterManager(interpreterManagerImplementation: InterpreterManagerImplementation):
            InterpreterManager

    companion object {
        @Provides
        @Singleton
        fun provideManageInterpreterStateUseCases(interpreterManager: InterpreterManager): ManageInterpreterStateUseCases =
            ManageInterpreterStateUseCases(
                StopInterpreterUseCase(interpreterManager),
                StartInterpreterUseCase(interpreterManager)
            )
    }
}