package com.chan.chat_client.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chan.chat_client.data.model.AuthInfoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DataStoreSession @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SessionStorage {
    object PreferencesKeys {
        val AUTH_INFO = stringPreferencesKey("auth_info")
    }

    override suspend fun get(): AuthInfoEntity? {
        return withContext(Dispatchers.IO) {
            dataStore.data.map { pref ->
                pref[PreferencesKeys.AUTH_INFO]?.let { Json.decodeFromString<AuthInfoEntity>(it) }
            }.first()
        }
    }

    override suspend fun set(info: AuthInfoEntity?) {
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                prefs[PreferencesKeys.AUTH_INFO] = Json.encodeToString(info)
            }
        }
    }
}