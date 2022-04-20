package com.hits.coded.data.modules

import com.hits.coded.data.models.sharedPreferences.SharedPreferencesUseCases
import com.hits.coded.data.repositoriesImplementations.SharedPreferencesRepositoryImplementation
import com.hits.coded.domain.repositories.SharedPreferencesRepository
import com.hits.coded.domain.useCases.sharedPreferences.ChangeOnboardingPassedStateUseCase
import com.hits.coded.domain.useCases.sharedPreferences.CheckIsOnboardingPassedUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPreferencesModule {
    @Binds
    abstract fun bindSharedPreferencesRepository(
        sharedPreferencesRepository: SharedPreferencesRepositoryImplementation
    ): SharedPreferencesRepository

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreferencesUseCases(
            sharedPreferencesRepository: SharedPreferencesRepository
        ): SharedPreferencesUseCases =
            SharedPreferencesUseCases(
                ChangeOnboardingPassedStateUseCase(sharedPreferencesRepository),
                CheckIsOnboardingPassedUseCase(sharedPreferencesRepository)
            )
    }
}