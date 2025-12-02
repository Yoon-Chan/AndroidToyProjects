package com.example.mediaeditex.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
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

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideMediaTransfer(@ApplicationContext context: Context): MediaTransfer {
        return AndroidMediaTransfer(context)
    }

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context) = context
}