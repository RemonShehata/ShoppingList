package com.example.shoppinglist.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_entity")
data class ShoppingEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "quantity")val quantity: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_bought") val isBought: Boolean
)