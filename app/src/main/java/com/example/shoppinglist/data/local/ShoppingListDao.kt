package com.example.shoppinglist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertShoppingItem(shoppingItemEntity: ShoppingItemEntity): Long

    @Query("SELECT * FROM ShoppingItemEntity")
    suspend fun getShoppingListItemsSync(): List<ShoppingItemEntity>

    @Query("SELECT * FROM ShoppingItemEntity")
     fun getShoppingListItemsFlow(): Flow<List<ShoppingItemEntity>>
}