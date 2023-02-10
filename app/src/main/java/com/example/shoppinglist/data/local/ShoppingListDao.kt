package com.example.shoppinglist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoppinglist.data.local.models.ShoppingItemEntity

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItemEntity: ShoppingItemEntity): Long

    @Query("SELECT * FROM ShoppingItemEntity")
    suspend fun getShoppingListItemsSync(): List<ShoppingItemEntity>
}