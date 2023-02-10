package com.example.shoppinglist.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItemEntity::class], version = DATABASE_VERSION)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}

private const val DATABASE_VERSION = 1