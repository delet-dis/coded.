package com.hits.coded.data.modules.interpreter

import com.hits.coded.data.implementations.repositories.interpreter.InterpreterConverterRepositoryImplementation
import com.hits.coded.data.models.interpreter.useCases.repositories.InterpreterConverterUseCases
import com.hits.coded.domain.repositories.interpreterRepositories.InterpreterConverterRepository
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToArrayBaseUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToBooleanUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToDoubleUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToIntUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToStringIndulgentlyUseCase
import com.hits.coded.domain.useCases.interpreter.convertors.ConvertAnyToStringUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterpreterConverterModule {
    @Binds
    abstract fun bindInterpreterConverterRepository(
        interpreterConverterRepository: InterpreterConverterRepositoryImplementation
    ): InterpreterConverterRepository

    companion object {
        @Provides
        @Singleton
        fun provideConverterUseCases(
            interpreterConverterRepository: InterpreterConverterRepository
        ): InterpreterConverterUseCases =
            InterpreterConverterUseCases(
                ConvertAnyToArrayBaseUseCase(interpreterConverterRepository),
                ConvertAnyToBooleanUseCase(interpreterConverterRepository),
                ConvertAnyToStringUseCase(interpreterConverterRepository),
                ConvertAnyToStringIndulgentlyUseCase(interpreterConverterRepository),
                ConvertAnyToIntUseCase(interpreterConverterRepository),
                ConvertAnyToDoubleUseCase(interpreterConverterRepository)
            )
    }
}