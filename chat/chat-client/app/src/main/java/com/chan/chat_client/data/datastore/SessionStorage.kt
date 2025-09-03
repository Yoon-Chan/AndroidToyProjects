package com.chan.chat_client.data.datastore

import com.chan.chat_client.data.model.AuthInfoEntity

interface SessionStorage {
    suspend fun get(): AuthInfoEntity?
    suspend fun set(info: AuthInfoEntity?)
}