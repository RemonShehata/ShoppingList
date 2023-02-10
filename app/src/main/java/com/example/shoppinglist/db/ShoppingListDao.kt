package com.example.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItemEntity: ShoppingItemEntity): Long

    @Query("SELECT * FROM ShoppingItemEntity")
    suspend fun getShoppingListItemsSync(): List<ShoppingItemEntity>
}