package com.example.shoppinglist.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.data.BoughtFilter
import com.example.shoppinglist.data.FilterPreferences
import com.example.shoppinglist.data.local.models.ShoppingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertShoppingItem(shoppingEntity: ShoppingEntity): Long

    @Query("SELECT * FROM shopping_entity")
    suspend fun getShoppingListItemsSync(): List<ShoppingEntity>

    @Query("SELECT * FROM shopping_entity")
     fun getShoppingListItemsFlow(): Flow<List<ShoppingEntity>>

    @Query("SELECT * FROM shopping_entity " +
            "WHERE name LIKE '%' || :searchQuery || '%' " +
            "OR description LIKE '%' || :searchQuery || '%'")
    fun getShoppingListItemsWithSearchFlow(searchQuery: String): Flow<List<ShoppingEntity>>

    @Query("SELECT * FROM shopping_entity WHERE is_bought = 0")
    fun getShoppingListNotBoughtItemsFlow(): Flow<List<ShoppingEntity>>

    @Query("SELECT * FROM shopping_entity WHERE is_bought = 1")
    fun getShoppingListBoughtItemsFlow(): Flow<List<ShoppingEntity>>

    @Query("UPDATE shopping_entity SET is_bought = :isBought WHERE name = :itemName")
     suspend fun updateBoughtStatus(itemName: String, isBought: Boolean): Int
    fun getShoppingListFlowWithFilter(boughtFilter: BoughtFilter): Flow<List<ShoppingEntity>> {
        Log.d("Remon", "getShoppingListFlowWithFilter: ${boughtFilter.name}")
        return when(boughtFilter){
            BoughtFilter.BOUGHT -> getShoppingListBoughtItemsFlow()
            BoughtFilter.NOT_BOUGHT -> getShoppingListNotBoughtItemsFlow()
            BoughtFilter.BOTH -> getShoppingListItemsFlow()
        }
    }

    @Delete
    suspend fun deleteShoppingItem(shoppingEntity: ShoppingEntity): Int

    @Update
    suspend fun updateShoppingItem(shoppingEntity: ShoppingEntity): Int
}