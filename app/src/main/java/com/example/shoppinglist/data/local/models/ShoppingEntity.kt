package com.example.shoppinglist.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "quantity")val quantity: Int,
    @ColumnInfo(name = "description") val description: String
)