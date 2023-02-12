package com.example.shoppinglist.di

import android.content.Context
import com.example.shoppinglist.data.PreferencesManager
import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.repos.ShoppingListRepo
import com.example.shoppinglist.data.repos.ShoppingListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun provideShoppingListRepo(dao: ShoppingListDao): ShoppingListRepo {
        return ShoppingListRepository(dao)
    }

    @Singleton
    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
}