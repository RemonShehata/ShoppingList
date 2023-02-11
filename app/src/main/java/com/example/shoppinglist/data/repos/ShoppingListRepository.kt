package com.example.shoppinglist.data.repos

import android.database.sqlite.SQLiteConstraintException
import com.example.shoppinglist.data.local.DuplicateItemException
import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Suppress("SwallowedException")
class ShoppingListRepository @Inject constructor(private val shoppingListDao: ShoppingListDao) :
    ShoppingListRepo {
    override suspend fun saveShoppingItem(item: ShoppingItemEntity): Long {
        return try {
            shoppingListDao.insertShoppingItem(item)
        } catch (ex: SQLiteConstraintException){
            throw DuplicateItemException("item ${item.name} already exists")
        }
    }

    override suspend fun getShoppingList(): List<ShoppingItemEntity> {
        return shoppingListDao.getShoppingListItemsSync()
    }

    override fun getShoppingListFlow(): Flow<List<ShoppingItemEntity>> {
        return shoppingListDao.getShoppingListItemsFlow()
    }
}