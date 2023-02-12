package com.example.shoppinglist.features.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.R
import com.example.shoppinglist.data.BoughtFilter
import com.example.shoppinglist.data.QueryError
import com.example.shoppinglist.data.SortOrder
import com.example.shoppinglist.data.State
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.databinding.FragmentShoppingListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingListFragment : Fragment(), MenuProvider {

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

            chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                onChipCheckedStateChanged(checkedIds)
            }

            sortChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                onSortChipCheckStateChanged(checkedIds)
            }
        }

        shoppingListViewModel.getShoppingListItemsUpdates()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    private fun onSortChipCheckStateChanged(checkedIds: List<Int>) {
        val sort: SortOrder = when (checkedIds) { // single select only is allowed
            listOf(R.id.ascChip) -> SortOrder.ASC
            listOf(R.id.dscChip) -> SortOrder.DESC
            else -> SortOrder.ASC
        }

        shoppingListViewModel.onSortOrderSelected(sort)
    }

    private fun onChipCheckedStateChanged(checkedIds: List<Int>) {
        val filter: BoughtFilter = when (checkedIds) {
            listOf(R.id.boughtChip) -> {
                BoughtFilter.BOUGHT
            }

            listOf(R.id.notBoughtChip) -> {
                BoughtFilter.NOT_BOUGHT
            }

            listOf(R.id.boughtChip, R.id.notBoughtChip), emptyList<Int>() -> {
                BoughtFilter.BOTH
            }
            else -> {
                BoughtFilter.BOTH
            }
        }

        Log.d("Remon", "onChipCheckedStateChanged: ${filter.name}")

        shoppingListViewModel.onBoughtFilterChanged(filter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingListAdapter = ShoppingListAdapter(onItemClicked, onCheckStateChanged)
        binding.shoppingListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shoppingListAdapter
//            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.HORIZONTAL))
        }


        with(shoppingListViewModel) {
            shoppingListLiveData.observe(viewLifecycleOwner, ::renderShoppingList)
            lifecycleScope.launch {
                val initValue = shoppingListViewModel.preferencesFlow.first()
                when (initValue.boughtFilter) {
                    BoughtFilter.BOUGHT -> binding.boughtChip.isChecked = true
                    BoughtFilter.NOT_BOUGHT -> binding.notBoughtChip.isChecked = true
                    BoughtFilter.BOTH -> {}
                }

                when (initValue.sortOrder) {
                    SortOrder.ASC -> {}
                    SortOrder.DESC -> {}
                }
            }
        }
    }

    private fun renderShoppingList(state: State<List<ShoppingEntity>, QueryError>) {
        when (state) {
            is State.Error -> TODO()

            State.Loading -> {

            }

            is State.Success -> {
                Log.d("Remon", "renderShoppingList: items: ${state.data}")
                shoppingListAdapter.submitList(state.data)
            }
        }
    }


    private val onItemClicked: (shoppingEntity: ShoppingEntity) -> Unit = { item ->
//        findNavController().navigate(
//            TrendingMoviesFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(
//                movieId
//            )
//        )
    }

    private val onCheckStateChanged: (itemName: String, isChecked: Boolean) -> Unit =
        { name, isChecked ->
            shoppingListViewModel.changeBoughtStatus(name, isChecked)
        }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.filterMenuButton -> {
                Log.d("Remon", "onMenuItemSelected: filterMenuButton")
                true
            }

            R.id.SortMenuButton -> {
                Log.d("Remon", "onMenuItemSelected: SortMenuButton")
                true
            }

            else -> false
        }
    }
}