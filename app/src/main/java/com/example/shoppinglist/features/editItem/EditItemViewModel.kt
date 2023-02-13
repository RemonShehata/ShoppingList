package com.example.shoppinglist.features.editItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.InsertionError
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.UpdateError
import com.example.shoppinglist.data.local.DuplicateItemException
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    private val editResultMutableLiveData = MutableLiveData<State<Nothing?, UpdateError>>()

    val editResultLiveData: LiveData<State<Nothing?, UpdateError>> = editResultMutableLiveData

    @Suppress("SwallowedException")
    fun saveShoppingItem(shoppingEntity: ShoppingEntity) {
        editResultMutableLiveData.value = State.Loading
        viewModelScope.launch {
            try {
                repo.updateItem(shoppingEntity)
                editResultMutableLiveData.postValue(State.Success(null))
            } catch (ex: DuplicateItemException) {
                editResultMutableLiveData.postValue(State.Error(UpdateError.Failure))
            }
        }
    }
}