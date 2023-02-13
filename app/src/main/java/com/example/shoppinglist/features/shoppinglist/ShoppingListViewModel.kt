package com.example.shoppinglist.features.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.*
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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


    fun getShoppingListItemsUpdates() {
        shoppingListMutableLiveData.value = State.Loading

        viewModelScope.launch {
            preferencesFlow.combine(repo.getShoppingListFlow()) { prefsFlow, listFlow ->
                prefsFlow
            }.collect {
                viewModelScope.launch {
                    val filter = it.boughtFilter
                    repo.getShoppingListFlowWithFilter(filter).collect { shoppingList ->
                        val sortedList = when (it.sortOrder) {
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
}

data class UpdatedItem(
    val itemName: String,
    val isBought: Boolean
)