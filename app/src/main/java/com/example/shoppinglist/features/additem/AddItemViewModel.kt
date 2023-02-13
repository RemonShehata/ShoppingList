package com.example.shoppinglist.features.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.InsertionError
import com.example.shoppinglist.data.InvalidField
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.DuplicateItemException
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    private val addResultMutableLiveData = MutableLiveData<State<Nothing?, InsertionError>>()

    val addResultLiveData: LiveData<State<Nothing?, InsertionError>> = addResultMutableLiveData

    @Suppress("SwallowedException")
    fun saveShoppingItem(shoppingEntity: ShoppingEntity) {
        addResultMutableLiveData.value = State.Loading
        if (shoppingEntity.name.isEmpty()) {
            addResultMutableLiveData.value =
                State.Error(InsertionError.InvalidData(InvalidField.ItemName))
            return
        } else if (shoppingEntity.quantity.isEmpty()) {
            addResultMutableLiveData.value =
                State.Error(InsertionError.InvalidData(InvalidField.ItemQuantity))
            return
        }

        viewModelScope.launch {
            try {
                repo.saveShoppingItem(shoppingEntity)
                addResultMutableLiveData.value = State.Success(null)
            } catch (ex: DuplicateItemException) {
                addResultMutableLiveData.value = State.Error(InsertionError.DuplicateItem)
            }
        }
    }
}