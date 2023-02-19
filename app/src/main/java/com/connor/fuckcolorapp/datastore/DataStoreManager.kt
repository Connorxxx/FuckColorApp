package com.connor.fuckcolorapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.connor.fuckcolorapp.extension.getCurrentThread
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val getDataStore = context.dataStore

    private val pureState = booleanPreferencesKey("pure_state")

    val pureFlow = getDataStore.data.map { it[pureState] ?: false }
        .flowOn(Dispatchers.IO)

    suspend fun storePureState(value: Boolean) {
        getDataStore.edit {
            withContext(Dispatchers.IO) { it[pureState] = value }
        }
    }
}