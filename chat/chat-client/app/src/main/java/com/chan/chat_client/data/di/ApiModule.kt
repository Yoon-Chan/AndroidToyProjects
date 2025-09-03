package com.chan.chat_client.data.di

import com.chan.chat_client.data.client.HttpClientFactory
import com.chan.chat_client.data.datastore.SessionStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        sessionStorage: SessionStorage
    ): HttpClient = HttpClientFactory(
        sessionStorage = sessionStorage
    ).build()

}