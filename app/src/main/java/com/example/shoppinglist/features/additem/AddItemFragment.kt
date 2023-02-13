package com.example.shoppinglist.features.additem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppinglist.data.InsertionError
import com.example.shoppinglist.data.InvalidField
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.databinding.FragmentAddItemBinding
import com.example.shoppinglist.utils.invisible
import com.example.shoppinglist.utils.showToast
import com.example.shoppinglist.utils.string
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
                    name = itemName.string(),
                    quantity = itemQuantity.string(),
                    description = itemDescription.string(),
                    isBought = false // not bought when first added.
                )

                addItemViewModel.saveShoppingItem(shoppingEntity)
            }

            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }

            itemName.doOnTextChanged { text, _, _, _ ->
                itemNameTextInputLayout.error = if (text.isNullOrEmpty()) EMPTY_FIELD_ERROR_MSG
                else null
            }
            itemQuantity.doOnTextChanged { text, _, _, _ ->
                itemQuantityTextInputLayout.error = if (text.isNullOrEmpty()) EMPTY_FIELD_ERROR_MSG
                else null
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
                                binding.itemNameTextInputLayout.error = EMPTY_FIELD_ERROR_MSG
                            }
                            InvalidField.ItemQuantity -> {
                                binding.itemQuantityTextInputLayout.error =
                                    EMPTY_FIELD_ERROR_MSG
                            }
                        }
                    }
                }
            }

            State.Loading -> binding.progressBar.visible()

            is State.Success -> {
                with(binding) {
                    progressBar.invisible()
                    showToast("Added item successfully")
                    findNavController().navigateUp()
                }
            }
        }
    }

    companion object {
        private const val EMPTY_FIELD_ERROR_MSG = "This field can't be empty"
    }
}