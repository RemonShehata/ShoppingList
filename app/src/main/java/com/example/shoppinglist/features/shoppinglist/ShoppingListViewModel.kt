package com.example.shoppinglist.features.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.InsertionError
import com.example.shoppinglist.data.QueryError
import com.example.shoppinglist.data.UpdateError
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    private val shoppingListMutableLiveData = MutableLiveData<State<List<ShoppingEntity>, QueryError>>()

    val shoppingListLiveData: LiveData<State<List<ShoppingEntity>, QueryError>> =
        shoppingListMutableLiveData

    private val itemUpdateMutableLiveData = MutableLiveData<State<Nothing?, UpdateError>>()

    val itemUpdateLiveData: LiveData<State<Nothing?, UpdateError>> =
        itemUpdateMutableLiveData

    fun getShoppingListItemsUpdates() {
        shoppingListMutableLiveData.value = State.Loading
        viewModelScope.launch {
            repo.getShoppingListFlow().collect {
                shoppingListMutableLiveData.value = State.Success(it)
            }
        }
    }

    fun changeBoughtStatus(itemName: String, isBought: Boolean) {
        itemUpdateMutableLiveData.value = State.Loading
        viewModelScope.launch {
            val result = repo.updateBoughtStatus(itemName, isBought)

            val valueToPost = if (result > 0) State.Success(null)
            else State.Error(UpdateError.Failure)

            itemUpdateMutableLiveData.postValue(valueToPost)
        }
    }
}