package com.example.shoppinglist.data.repos

import android.database.sqlite.SQLiteConstraintException
import com.example.shoppinglist.data.BoughtFilter
import com.example.shoppinglist.data.local.DuplicateItemException
import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.local.models.ShoppingEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Suppress("SwallowedException")
class ShoppingListRepository @Inject constructor(private val shoppingListDao: ShoppingListDao) :
    ShoppingListRepo {
    override suspend fun saveShoppingItem(item: ShoppingEntity): Long {
        return try {
            shoppingListDao.insertShoppingItem(item)
        } catch (ex: SQLiteConstraintException) {
            throw DuplicateItemException("item ${item.name} already exists")
        }
    }

    override suspend fun getShoppingList(): List<ShoppingEntity> {
        return shoppingListDao.getShoppingListItemsSync()
    }

    override fun getShoppingListFlow(): Flow<List<ShoppingEntity>> {
        return shoppingListDao.getShoppingListItemsFlow()

    }

    override fun getShoppingListItemsWithSearchFlow(
        searchQuery: String,
        boughtFilter: BoughtFilter
    ): Flow<List<ShoppingEntity>> {
        return shoppingListDao.getShoppingListWithFiltersFlow(searchQuery, boughtFilter)
    }

    override suspend fun updateBoughtStatus(itemName: String, isBought: Boolean): Int {
        return shoppingListDao.updateBoughtStatus(itemName, isBought)
    }

    override suspend fun removeItem(shoppingEntity: ShoppingEntity): Int {
        return shoppingListDao.deleteShoppingItem(shoppingEntity)
    }

    override suspend fun updateItem(shoppingEntity: ShoppingEntity): Int {
        return shoppingListDao.updateShoppingItem(shoppingEntity)
    }
}