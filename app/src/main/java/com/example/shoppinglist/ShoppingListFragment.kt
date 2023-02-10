package com.example.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shoppinglist.databinding.FragmentShoppingListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingListBinding.inflate(layoutInflater).apply {
            floatingActionButton.setOnClickListener {
                findNavController()
                    .navigate(
                        ShoppingListFragmentDirections.actionShoppingListFragmentToAddItemFragment()
                    )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}