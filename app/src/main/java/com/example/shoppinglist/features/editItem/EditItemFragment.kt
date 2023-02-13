package com.example.shoppinglist.features.editItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.UpdateError
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.databinding.FragmentEditItemBinding
import com.example.shoppinglist.utils.invisible
import com.example.shoppinglist.utils.showToast
import com.example.shoppinglist.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private lateinit var binding: FragmentEditItemBinding

    private val editItemViewModel: EditItemViewModel by viewModels()

    private val args: EditItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditItemBinding.inflate(layoutInflater).apply {

            loadInitDataOnUI()

            updateButton.setOnClickListener {
                val shoppingEntity = ShoppingEntity(
                    name = binding.itemName.text.toString(),
                    quantity = binding.itemQuantity.text.toString().toInt(),
                    description = binding.itemDescription.text.toString(),
                    isBought = itemName.isChecked
                )

                editItemViewModel.saveShoppingItem(shoppingEntity)
            }

            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    private fun FragmentEditItemBinding.loadInitDataOnUI() {
        itemName.text = args.itemName
        itemQuantity.setText(args.itemQuantity.toString())
        itemDescription.setText(args.itemDescription)
        itemName.isChecked = args.isItemBought
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(editItemViewModel) {
            editResultLiveData.observe(viewLifecycleOwner, ::onUpdateCompleted)
        }
    }

    private fun onUpdateCompleted(state: State<Nothing?, UpdateError>) {
        when (state) {
            is State.Error -> {
                binding.progressBar.invisible()
                when (state.errorType) {
                    UpdateError.Failure -> showToast("Error while updating...try again")
                }
            }

            State.Loading -> binding.progressBar.visible()

            is State.Success -> {
                with(binding) {
                    progressBar.invisible()
                    findNavController().navigateUp()
                    showToast("Updated item successfully")
                }
            }
        }
    }
}