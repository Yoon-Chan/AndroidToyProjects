package com.example.media3ex.di

import android.content.Context
import com.example.media3ex.data.AndroidMediaPlayer
import com.example.media3ex.domain.MediaController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Singleton
    @Provides
    fun provideMediaController(
        @ApplicationContext context: Context
    ): MediaController {
        return AndroidMediaPlayer(context)
    }
}