package com.chan.chat_client.data.di

import com.chan.chat_client.data.repository.ChatRepositoryImpl
import com.chan.chat_client.data.repository.LoginRepositoryImpl
import com.chan.chat_client.data.repository.UserRepositoryImpl
import com.chan.chat_client.domain.repository.ChatRepository
import com.chan.chat_client.domain.repository.LoginRepository
import com.chan.chat_client.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(repositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindLoginChatRoomRepository(repositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository
}