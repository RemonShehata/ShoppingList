package com.example.shoppinglist.features.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.databinding.FragmentShoppingListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListBinding

    private lateinit var shoppingListAdapter: ShoppingListAdapter

    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

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

        shoppingListViewModel.getShoppingListItemsUpdates()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingListAdapter = ShoppingListAdapter(onItemClicked, emptyList())
        binding.shoppingListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shoppingListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.HORIZONTAL))
        }


        with(shoppingListViewModel){
            shoppingListLiveData.observe(viewLifecycleOwner, ::renderShoppingList)
        }
    }

    private fun renderShoppingList(state: State<List<ShoppingItemEntity>>){
        when(state){
            is State.Error -> TODO()
            State.Loading -> {

            }
            is State.Success -> {
                Log.d("Remon", "renderShoppingList: items: ${state.data}")
                shoppingListAdapter.setItems(state.data)
            }
        }
    }


    private val onItemClicked: (shoppingItemEntity: ShoppingItemEntity) -> Unit = { item ->
//        findNavController().navigate(
//            TrendingMoviesFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(
//                movieId
//            )
//        )
    }
}