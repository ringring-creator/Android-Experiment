package com.ring.ring.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ring.ring.log.Logger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreUserDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val logger: Logger,
) : UserLocalDataSource {
    override suspend fun save(user: LocalUser) {
        try {
            dataStore.edit {
                it[USER_ID_KEY] = user.userId
                it[EMAIL_KEY] = user.email
                it[TOKEN_KEY] = user.token
            }
        } catch (e: Throwable) {
            logger.e("DataStoreUserDataSource", "failed to save", e)
            throw e
        }
    }

    override suspend fun getUser(): LocalUser? {
        return try {
            dataStore.data.map {
                LocalUser(
                    userId = it[USER_ID_KEY] ?: return@map null,
                    email = it[EMAIL_KEY] ?: return@map null,
                    token = it[TOKEN_KEY] ?: return@map null,
                )
            }.first()
        } catch (e: Throwable) {
            logger.e("DataStoreUserDataSource", "failed to getUser", e)
            throw e
        }
    }

    companion object {
        private val USER_ID_KEY = longPreferencesKey("userId")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}