package com.example.shoppinglist.features.additem

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.databinding.FragmentAddItemBinding
import com.example.shoppinglist.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding

    private val addItemViewModel: AddItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(layoutInflater).apply {

            saveButton.setOnClickListener {
                val shoppingItemEntity = ShoppingItemEntity(
                    name = binding.itemName.text.toString(),
                    quantity = binding.itemQuantity.text.toString().toInt(),
                    description = binding.itemDescription.text.toString()
                )

                addItemViewModel.saveShoppingItem(shoppingItemEntity)
            }

            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(addItemViewModel) {
            addResultLiveData.observe(viewLifecycleOwner, ::onAdditionCompleted)
        }
    }

    private fun onAdditionCompleted(state: State<Int>) {
        when (state) {
            is State.Error -> TODO()
            State.Loading -> TODO()
            is State.Success -> {
                with(binding){
                    itemName.text?.clear()
                    itemQuantity.text?.clear()
                    itemDescription.text?.clear()
                    showToast("Added item successfully")
                }
            }
        }
    }
}