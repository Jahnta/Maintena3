package com.db.maintena.di

import com.db.maintena.data.settings.DefaultSettingsRepository
import com.db.maintena.data.settings.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        repository: DefaultSettingsRepository,
    ): SettingsRepository
}
