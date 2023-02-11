package com.example.shoppinglist.features.additem

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
class AddItemViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    private val addResultMutableLiveData = MutableLiveData<State<Nothing?, InsertionError>>()

    val addResultLiveData: LiveData<State<Nothing?, InsertionError>> = addResultMutableLiveData

    @Suppress("SwallowedException")
    fun saveShoppingItem(shoppingEntity: ShoppingEntity) {
        addResultMutableLiveData.value = State.Loading
        viewModelScope.launch {
            try {
                repo.saveShoppingItem(shoppingEntity)
                addResultMutableLiveData.postValue(State.Success(null))
            } catch (ex: DuplicateItemException) {
                addResultMutableLiveData.postValue(State.Error(InsertionError.DuplicateItem))
            }
        }
    }
}