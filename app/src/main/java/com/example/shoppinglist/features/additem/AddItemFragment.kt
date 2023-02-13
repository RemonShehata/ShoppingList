package com.example.shoppinglist.features.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppinglist.data.InsertionError
import com.example.shoppinglist.data.InvalidField
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.databinding.FragmentAddItemBinding
import com.example.shoppinglist.utils.string
import com.example.shoppinglist.utils.invisible
import com.example.shoppinglist.utils.showToast
import com.example.shoppinglist.utils.visible
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
                val shoppingEntity = ShoppingEntity(
                    name = binding.itemName.string(),
                    quantity = binding.itemQuantity.text.toString(),
                    description = binding.itemDescription.text.toString(),
                    isBought = false // not bought when first added.
                )

                addItemViewModel.saveShoppingItem(shoppingEntity)
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

    private fun onAdditionCompleted(state: State<Nothing?, InsertionError>) {
        when (state) {
            is State.Error -> {
                binding.progressBar.invisible()
                when (state.errorType) {
                    InsertionError.DuplicateItem -> showToast("Item already exist, you can modify it")
                    is InsertionError.InvalidData -> {
                        when (state.errorType.fields) {
                            InvalidField.ItemName -> {
                                binding.itemNameTextInputLayout.error = "This field can't be empty"
                            }
                            InvalidField.ItemQuantity -> {
                                binding.itemQuantityTextInputLayout.error =
                                    "This field can't be empty"
                            }
                        }
                    }
                }
            }

            State.Loading -> binding.progressBar.visible()

            is State.Success -> {
                with(binding) {
                    progressBar.invisible()
                    itemName.text?.clear()
                    itemQuantity.text?.clear()
                    itemDescription.text?.clear()
                    showToast("Added item successfully")
                }
            }
        }
    }
}