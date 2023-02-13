package com.example.shoppinglist.data.local

import androidx.room.*
import com.example.shoppinglist.data.BoughtFilter
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

    @Query(
        "SELECT * FROM shopping_entity " +
                "WHERE (is_bought = :isBought) " +
                "AND " +
                "(name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%')"
    )
    fun getShoppingListItemsWithSearchFlow(
        searchQuery: String,
        isBought: Int
    ): Flow<List<ShoppingEntity>>

    @Query(
        "SELECT * FROM shopping_entity " +
                "WHERE name LIKE '%' || :searchQuery || '%' " +
                "OR description LIKE '%' || :searchQuery || '%'"
    )
    fun getShoppingListItemsWithSearchFlow(
        searchQuery: String,
    ): Flow<List<ShoppingEntity>>

    fun getShoppingListWithFiltersFlow(
        query: String,
        boughtFilter: BoughtFilter
    ): Flow<List<ShoppingEntity>> {
        return when (boughtFilter) {
            BoughtFilter.BOUGHT -> getShoppingListItemsWithSearchFlow(query, 1)
            BoughtFilter.NOT_BOUGHT -> getShoppingListItemsWithSearchFlow(query, 0)
            BoughtFilter.BOTH -> getShoppingListItemsWithSearchFlow(query)
        }
    }


    @Query("UPDATE shopping_entity SET is_bought = :isBought WHERE name = :itemName")
    suspend fun updateBoughtStatus(itemName: String, isBought: Boolean): Int

    @Delete
    suspend fun deleteShoppingItem(shoppingEntity: ShoppingEntity): Int

    @Update
    suspend fun updateShoppingItem(shoppingEntity: ShoppingEntity): Int
}