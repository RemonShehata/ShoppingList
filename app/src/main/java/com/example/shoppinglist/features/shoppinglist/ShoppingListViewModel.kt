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
import kotlinx.coroutines.Job
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

    private var notBoughtJob: Job? = null
    private var boughtJob: Job? = null
    private var shoppingListJob: Job? = null

    fun getShoppingListItemsUpdates() {
        shoppingListMutableLiveData.value = State.Loading

        notBoughtJob?.cancel()
        boughtJob?.cancel()

        shoppingListJob = viewModelScope.launch {
            repo.getShoppingListFlow().collect {
                shoppingListMutableLiveData.value = State.Success(it)
            }
        }
    }

    fun getShoppingListBoughtItemsUpdates(){
        shoppingListMutableLiveData.value = State.Loading

        notBoughtJob?.cancel()
        shoppingListJob?.cancel()

        boughtJob = viewModelScope.launch {
            repo.getShoppingListBoughtItemsFlow().collect {
                shoppingListMutableLiveData.value = State.Success(it)
            }
        }
    }

    fun getShoppingListNotBoughtItemsUpdates(){
        shoppingListMutableLiveData.value = State.Loading

        boughtJob?.cancel()
        shoppingListJob?.cancel()

        notBoughtJob = viewModelScope.launch {
            repo.getShoppingListNotBoughtItemsFlow().collect {
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