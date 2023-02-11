package com.example.shoppinglist.data.repos

import com.example.shoppinglist.data.local.models.ShoppingEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepo {
    suspend fun saveShoppingItem(item: ShoppingEntity): Long
    suspend fun getShoppingList(): List<ShoppingEntity>
    fun getShoppingListFlow(): Flow<List<ShoppingEntity>>

    suspend fun updateBoughtStatus(itemName: String, isBought: Boolean): Int
}