package com.example.shoppinglist.features.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    private val shoppingListMutableLiveData = MutableLiveData<State<List<ShoppingItemEntity>>>()

    val shoppingListLiveData: LiveData<State<List<ShoppingItemEntity>>> =
        shoppingListMutableLiveData

    fun getShoppingListItemsUpdates() {
        shoppingListMutableLiveData.value = State.Loading
        viewModelScope.launch {
            repo.getShoppingListFlow().collect {
                shoppingListMutableLiveData.value = State.Success(it)
            }
        }
    }
}