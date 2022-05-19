package com.hits.coded.data.modules.interpreter

import com.hits.coded.data.implementations.helpers.StopInterpreterImplementation
import com.hits.coded.data.models.interpreter.useCases.helpers.ManageInterpreterStateUseCases
import com.hits.coded.domain.repositories.interpreterRepositories.helpers.StopInterpreter
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
abstract class StopInterpreterModule {
    @Binds
    abstract fun bindStopInterpreter(stopInterpreter: StopInterpreterImplementation): StopInterpreter

    companion object {
        @Provides
        @Singleton
        fun provideStopInterpreterUseCases(stopInterpreter: StopInterpreter): ManageInterpreterStateUseCases =
            ManageInterpreterStateUseCases(
                StopInterpreterUseCase(stopInterpreter),
                StartInterpreterUseCase(stopInterpreter)
            )
    }
}