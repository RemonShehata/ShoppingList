package com.example.shoppinglist.features.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ErrorType
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.DuplicateItemException
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(private val repo: ShoppingListRepo) : ViewModel() {

    private val addResultMutableLiveData = MutableLiveData<State<Int>>() //TODO: pass nothing

    val addResultLiveData: LiveData<State<Int>> = addResultMutableLiveData

    @Suppress("SwallowedException")
    fun saveShoppingItem(shoppingItemEntity: ShoppingItemEntity) {
        viewModelScope.launch {
            try {
                repo.saveShoppingItem(shoppingItemEntity)
                addResultMutableLiveData.postValue(State.Success(1))
            } catch (ex: DuplicateItemException) {
                addResultMutableLiveData.postValue(State.Error(ErrorType.DuplicateItem))
            }
        }
    }
}