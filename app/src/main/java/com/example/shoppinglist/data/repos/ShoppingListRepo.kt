package com.example.shoppinglist.data.repos

import com.example.shoppinglist.data.local.models.ShoppingItemEntity

interface ShoppingListRepo {
    suspend fun saveShoppingItem(item: ShoppingItemEntity): Long
    suspend fun getShoppingList(): List<ShoppingItemEntity>
}