package com.example.shoppinglist.data.repos

import com.example.shoppinglist.data.BoughtFilter
import com.example.shoppinglist.data.local.models.ShoppingEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepo {
    suspend fun saveShoppingItem(item: ShoppingEntity): Long
    suspend fun getShoppingList(): List<ShoppingEntity>
    fun getShoppingListFlow(): Flow<List<ShoppingEntity>>

    fun getShoppingListNotBoughtItemsFlow(): Flow<List<ShoppingEntity>>

    fun getShoppingListBoughtItemsFlow(): Flow<List<ShoppingEntity>>

    fun getShoppingListFlowWithFilter(filterPreferences: BoughtFilter): Flow<List<ShoppingEntity>>
    fun getShoppingListItemsWithSearchFlow(
        searchQuery: String,
        boughtFilter: BoughtFilter
    ): Flow<List<ShoppingEntity>>

    suspend fun updateBoughtStatus(itemName: String, isBought: Boolean): Int
    suspend fun removeItem(shoppingEntity: ShoppingEntity): Int

    suspend fun updateItem(shoppingEntity: ShoppingEntity): Int
}
