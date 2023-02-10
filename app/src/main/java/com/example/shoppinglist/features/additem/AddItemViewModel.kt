package com.example.shoppinglist.features.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    fun saveShoppingItem(shoppingItemEntity: ShoppingItemEntity){
        viewModelScope.launch {
            val result = repo.saveShoppingItem(shoppingItemEntity)
        }
    }
}