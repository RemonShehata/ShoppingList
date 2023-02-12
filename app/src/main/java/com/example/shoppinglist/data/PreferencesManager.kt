package com.example.shoppinglist.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

enum class SortOrder { ASC, DESC }
enum class BoughtFilter { BOUGHT, NOT_BOUGHT, BOTH }

data class FilterPreferences(val sortOrder: SortOrder, val boughtFilter: BoughtFilter)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


    val preferencesFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.DESC.name
            )

            val boughtFilter =
                BoughtFilter.valueOf(
                    preferences[PreferencesKeys.BOUGHT_FILTER] ?: BoughtFilter.NOT_BOUGHT.name
                )

            FilterPreferences(sortOrder, boughtFilter)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideBought(boughtFilter: BoughtFilter) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BOUGHT_FILTER] = boughtFilter.name
        }
    }


    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val BOUGHT_FILTER = stringPreferencesKey("hide_bought")
    }
}
