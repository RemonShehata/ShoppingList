package com.example.shoppinglist.features.shoppinglist

import android.util.Log
import androidx.lifecycle.*
import com.example.shoppinglist.data.*
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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

    private val itemUpdateMutableLiveData = MutableLiveData<State<UpdatedItem, UpdateError>>()

    val itemUpdateLiveData: LiveData<State<UpdatedItem, UpdateError>> =
        itemUpdateMutableLiveData

    private val itemDeleteMutableLiveData = MutableLiveData<State<Nothing?, UpdateError>>()

    val itemDeleteLiveData: LiveData<State<Nothing?, UpdateError>> =
        itemDeleteMutableLiveData

    val searchQuery = MutableStateFlow("")


    fun getShoppingListItemsUpdates() {
        shoppingListMutableLiveData.value = State.Loading

        viewModelScope.launch {
            combine(
                preferencesFlow,
                repo.getShoppingListFlow(),
                searchQuery
            ) { prefsFlow, listFlow, query ->
                Pair(prefsFlow, query)
            }.collect {
                val prefsFlow = it.first
                val filter = prefsFlow.boughtFilter
                val query = it.second

                Log.d("Remon", "getShoppingListItemsUpdates: filter = ${filter.name}")
                Log.d("Remon", "getShoppingListItemsUpdates: query = $query")

                viewModelScope.launch {
                    repo.getShoppingListItemsWithSearchFlow(query, filter).collect { shoppingList ->
                        val sortedList = when (prefsFlow.sortOrder) {
                            SortOrder.ASC -> shoppingList.sortedWith(compareBy { item -> item.name })
                            SortOrder.DESC -> shoppingList.sortedWith(compareByDescending { item -> item.name })
                        }
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
            val updatedItem = UpdatedItem(itemName, isBought)

            val valueToPost = if (result > 0) State.Success(updatedItem)
            else State.Error(UpdateError.Failure)

            itemUpdateMutableLiveData.value = valueToPost
        }
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onBoughtFilterChanged(boughtFilter: BoughtFilter) = viewModelScope.launch {
        preferencesManager.updateHideBought(boughtFilter)
    }

    fun onDeleteClicked(shoppingEntity: ShoppingEntity) {
        itemDeleteMutableLiveData.value = State.Loading
        viewModelScope.launch {
            val result = repo.removeItem(shoppingEntity)

            val valueToPost = if (result > 0) State.Success(null)
            else State.Error(UpdateError.Failure)

            itemDeleteMutableLiveData.value = valueToPost
        }
    }
}

data class UpdatedItem(
    val itemName: String,
    val isBought: Boolean
)