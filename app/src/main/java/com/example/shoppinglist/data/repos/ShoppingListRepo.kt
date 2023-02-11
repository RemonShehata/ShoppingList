package com.example.shoppinglist.data.repos

import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepo {
    suspend fun saveShoppingItem(item: ShoppingItemEntity): Long
    suspend fun getShoppingList(): List<ShoppingItemEntity>
    fun getShoppingListFlow(): Flow<List<ShoppingItemEntity>>
}