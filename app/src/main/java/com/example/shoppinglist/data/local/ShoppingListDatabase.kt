package com.example.shoppinglist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoppinglist.data.local.models.ShoppingItemEntity

@Database(entities = [ShoppingItemEntity::class], version = DATABASE_VERSION)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}

private const val DATABASE_VERSION = 1