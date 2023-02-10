package com.example.shoppinglist.features.shoppinglist

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.repos.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(repo: ShoppingListRepo) : ViewModel() {

}