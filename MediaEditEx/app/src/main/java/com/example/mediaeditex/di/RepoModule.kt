package com.example.mediaeditex.di

import android.content.Context
import com.example.mediaeditex.data.RecordRepositoryImpl
import com.example.mediaeditex.domain.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindsRecordRepository(
        recordRepositoryImpl: RecordRepositoryImpl
    ): RecordRepository

}