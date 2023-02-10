package com.example.shoppinglist.features.additem

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(repo: ShoppingListRepo) : ViewModel() {

}