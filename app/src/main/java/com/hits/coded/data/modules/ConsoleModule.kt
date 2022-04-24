package com.hits.coded.data.modules

import com.hits.coded.data.models.console.ConsoleUseCases
import com.hits.coded.data.repositoriesImplementations.ConsoleRepositoryImplementation
import com.hits.coded.domain.repositories.ConsoleRepository
import com.hits.coded.domain.useCases.console.GetBufferUseCase
import com.hits.coded.domain.useCases.console.ClearConsoleUseCase
import com.hits.coded.domain.useCases.console.CheckIsInputAvailableUseCase
import com.hits.coded.domain.useCases.console.ReadFromConsoleUseCase
import com.hits.coded.domain.useCases.console.WriteToConsoleUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ConsoleModule {
    @Binds
    abstract fun binConsoleRepository(
        consoleRepository: ConsoleRepositoryImplementation
    ): ConsoleRepository

    companion object {
        @Provides
        @Singleton
        fun provideConsoleUseCases(consoleRepository: ConsoleRepository): ConsoleUseCases =
            ConsoleUseCases(
                CheckIsInputAvailableUseCase(consoleRepository),
                ClearConsoleUseCase(consoleRepository),
                GetBufferUseCase(consoleRepository),
                ReadFromConsoleUseCase(consoleRepository),
                WriteToConsoleUseCase(consoleRepository)
            )
    }
}