package com.ring.ring.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class User(
    val userId: Long,
    val email: String,
    val token: String,
)

@Singleton
class DataStoreUserDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserLocalDataSource {
    override suspend fun save(user: User) {
        dataStore.edit {
            it[USER_ID_KEY] = user.userId
            it[EMAIL_KEY] = user.email
            it[TOKEN_KEY] = user.token
        }
    }

    override suspend fun getUser(): User? {
        return dataStore.data.map {
            User(
                userId = it[USER_ID_KEY] ?: return@map null,
                email = it[EMAIL_KEY] ?: return@map null,
                token = it[TOKEN_KEY] ?: return@map null,
            )
        }.first()
    }

    companion object {
        private val USER_ID_KEY = longPreferencesKey("userId")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}