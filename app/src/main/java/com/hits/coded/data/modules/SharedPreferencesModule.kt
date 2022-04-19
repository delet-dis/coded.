package com.hits.coded.data.modules

import com.hits.coded.data.models.SharedPreferencesUseCases
import com.hits.coded.data.repositoriesImplementations.SharedPreferencesRepositoryImplementation
import com.hits.coded.domain.useCases.sharedPreferences.ChangeOnboardingPassedStateUseCase
import com.hits.coded.domain.useCases.sharedPreferences.CheckIsOnboardingPassedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideSharedPreferencesUseCases(sharedPreferencesRepository: SharedPreferencesRepositoryImplementation): SharedPreferencesUseCases =
        SharedPreferencesUseCases(ChangeOnboardingPassedStateUseCase(sharedPreferencesRepository), CheckIsOnboardingPassedUseCase(sharedPreferencesRepository))
}