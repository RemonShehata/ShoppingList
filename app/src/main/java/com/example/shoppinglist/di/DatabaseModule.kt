package com.example.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.local.ShoppingListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideShoppingListDatabase(@ApplicationContext applicationContext: Context): ShoppingListDatabase {
        return Room.databaseBuilder(
            applicationContext.applicationContext,
            ShoppingListDatabase::class.java, MOVIES_DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideShoppingListDao(moviesDatabase: ShoppingListDatabase): ShoppingListDao {
        return moviesDatabase.shoppingListDao()
    }

    private const val MOVIES_DATABASE_NAME = "shopping_list.db"

}