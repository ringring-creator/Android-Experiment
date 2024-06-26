package com.ring.ring.user.infra.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.log.Logger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStoreDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val logger: Logger,
) : UserLocalDataSource {
    override suspend fun save(user: User) {
        try {
            dataStore.edit {
                it[USER_ID_KEY] = user.id.value
                it[EMAIL_KEY] = user.email.value
                //Todo encrypt
                it[PASSWORD_KEY] = user.password.value
                it[TOKEN_KEY] = user.token
            }
        } catch (e: Throwable) {
            logger.e("DataStoreUserDataSource", "failed to save", e)
            throw e
        }
    }

    override suspend fun getUser(): User? {
        return try {
            dataStore
                .data
                .map(this::toUser)
                .first()
        } catch (e: Throwable) {
            logger.e("DataStoreUserDataSource", "failed to getUser", e)
            throw e
        }
    }

    override suspend fun delete() {
        try {
            dataStore.edit {
                it.remove(USER_ID_KEY)
                it.remove(EMAIL_KEY)
                it.remove(PASSWORD_KEY)
                it.remove(TOKEN_KEY)
            }
        } catch (e: Throwable) {
            logger.e("DataStoreUserDataSource", "failed to delete", e)
            throw e
        }
    }

    private fun toUser(it: Preferences): User? {
        val userId = it[USER_ID_KEY] ?: return null
        val email = it[EMAIL_KEY] ?: return null
        val password = it[PASSWORD_KEY] ?: return null
        val token = it[TOKEN_KEY] ?: return null
        return User.generate(
            id = userId,
            email = email,
            password = password,
            token = token,
        )
    }

    companion object {
        private val USER_ID_KEY = longPreferencesKey("userId")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}