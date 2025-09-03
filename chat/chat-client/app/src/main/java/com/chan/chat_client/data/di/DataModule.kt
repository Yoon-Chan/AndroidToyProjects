package com.chan.chat_client.data.di

import com.chan.chat_client.data.datastore.DataStoreSession
import com.chan.chat_client.data.datastore.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindSessionStorage(
        dataStoreSession: DataStoreSession
    ): SessionStorage

}