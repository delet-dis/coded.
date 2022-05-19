package com.hits.coded.data.modules.interpreter

import com.hits.coded.data.implementations.helpers.InterpreterHelperImplementation
import com.hits.coded.data.models.interpreter.useCases.helpers.InterpreterHelperUseCases
import com.hits.coded.domain.repositories.interpreterRepositories.helpers.InterpreterHelper
import com.hits.coded.domain.useCases.interpreter.helpers.GetCurrentIdVariableUseCase
import com.hits.coded.domain.useCases.interpreter.helpers.SetCurrentIdVariableUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterHelperModule {
    @Binds
    abstract fun bindInterpreterHelper(interpreterHelper: InterpreterHelperImplementation): InterpreterHelper

    companion object {
        @Provides
        @Singleton
        fun provideInterpreterHelperUseCases(interpreterHelper: InterpreterHelper): InterpreterHelperUseCases =
            InterpreterHelperUseCases(
                GetCurrentIdVariableUseCase(interpreterHelper),
                SetCurrentIdVariableUseCase(interpreterHelper)
            )
    }
}