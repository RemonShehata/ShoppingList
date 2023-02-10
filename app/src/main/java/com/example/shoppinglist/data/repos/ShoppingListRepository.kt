package com.example.shoppinglist.data.repos

import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(private val shoppingListDao: ShoppingListDao) :
    ShoppingListRepo {
    override suspend fun saveShoppingItem(item: ShoppingItemEntity): Long {
        return shoppingListDao.insertShoppingItem(item)
    }

    override suspend fun getShoppingList(): List<ShoppingItemEntity> {
        return shoppingListDao.getShoppingListItemsSync()
    }
}