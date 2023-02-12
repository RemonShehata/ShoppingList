package com.example.shoppinglist.features.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.*
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repo: ShoppingListRepo,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val preferencesFlow = preferencesManager.preferencesFlow

    private val shoppingListMutableLiveData =
        MutableLiveData<State<List<ShoppingEntity>, QueryError>>()

    val shoppingListLiveData: LiveData<State<List<ShoppingEntity>, QueryError>> =
        shoppingListMutableLiveData

    private val itemUpdateMutableLiveData = MutableLiveData<State<Nothing?, UpdateError>>()

    val itemUpdateLiveData: LiveData<State<Nothing?, UpdateError>> =
        itemUpdateMutableLiveData


    fun getShoppingListItemsUpdates() {
        shoppingListMutableLiveData.value = State.Loading


        viewModelScope.launch {
            preferencesFlow.collect { filter ->
                Log.d("Remon", "getShoppingListItemsUpdates: filter = $filter")
                viewModelScope.launch {
                    repo.getShoppingListFlowWithFilter(filter).collect { shoppingList ->
                        Log.d("Remon", "getShoppingListItemsUpdates: list = $shoppingList")
                        val sortedList = when (filter.sortOrder) {
                            SortOrder.ASC -> shoppingList.sortedWith(compareBy { it.name })
                            SortOrder.DESC -> shoppingList.sortedWith(compareByDescending { it.name })
                        }

                        Log.d("Remon", "getShoppingListItemsUpdates: sortedList = $sortedList")

                        shoppingListMutableLiveData.value = State.Success(sortedList)

                    }
                }

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

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onBoughtFilterChanged(boughtFilter: BoughtFilter) = viewModelScope.launch {
        preferencesManager.updateHideBought(boughtFilter)
    }
}