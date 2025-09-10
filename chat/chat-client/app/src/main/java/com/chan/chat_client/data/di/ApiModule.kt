package com.chan.chat_client.data.di

import com.chan.chat_client.data.client.HttpClientFactory
import com.chan.chat_client.data.datastore.SessionStorage
import com.chan.chat_client.data.repository.DefaultStompMessageClient
import com.chan.chat_client.domain.repository.StompMessageClient
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

    @Provides
    @Singleton
    fun provideStompMessageClient(httpClient: HttpClient, sessionStorage: SessionStorage): StompMessageClient =
        DefaultStompMessageClient(httpClient, sessionStorage)
}