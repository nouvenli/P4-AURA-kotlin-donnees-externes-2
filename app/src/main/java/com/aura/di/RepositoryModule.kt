package com.aura.di

import com.aura.data.repository.AuraRepositoryImpl
import com.aura.domain.repository.AuraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuraRepository(
        auraRepositoryImpl: AuraRepositoryImpl
    ): AuraRepository
}