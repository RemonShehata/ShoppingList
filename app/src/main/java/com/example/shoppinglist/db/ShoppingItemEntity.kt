package com.example.shoppinglist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "quantity")val quantity: Int,
    @ColumnInfo(name = "description") val description: String
)