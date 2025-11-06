package com.example.mediaeditex.di

import android.content.Context
import com.example.mediaeditex.data.AndroidMediaTransfer
import com.example.mediaeditex.domain.MediaTransfer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideMediaTransfer(@ApplicationContext context: Context): MediaTransfer {
        return AndroidMediaTransfer(context)
    }
}