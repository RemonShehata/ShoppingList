package com.example.shoppinglist.di

import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.repos.ShoppingListRepo
import com.example.shoppinglist.data.repos.ShoppingListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}