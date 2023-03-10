package com.example.shoppinglist.features.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.R
import com.example.shoppinglist.data.*
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.databinding.FragmentShoppingListBinding
import com.example.shoppinglist.utils.invisible
import com.example.shoppinglist.utils.onQueryTextChanged
import com.example.shoppinglist.utils.showToast
import com.example.shoppinglist.utils.visible
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
                shoppingListViewModel.onAddClicked()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingListAdapter =
            ShoppingListAdapter(onItemClicked, onCheckStateChanged, onDeleteItemClicked)
        shoppingListAdapter.setHasStableIds(true)
        binding.shoppingListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shoppingListAdapter
            setHasFixedSize(true)
        }


        with(shoppingListViewModel) {
            shoppingListLiveData.observe(viewLifecycleOwner, ::renderShoppingList)
            itemUpdateLiveData.observe(viewLifecycleOwner, ::onItemStateUpdated)
            lifecycleScope.launch {
                onFilterPreferenceUpdated(preferencesFlow.first())
            }

            lifecycleScope.launch {
                navigationSharedFlow
                    .collect { nav ->
                        handleNavigation(nav)
                    }
            }
        }
    }

    private fun handleNavigation(nav: ShoppingListNavigation) {
        when (nav) {
            ShoppingListNavigation.AddItem -> {
                findNavController()
                    .navigate(
                        ShoppingListFragmentDirections.actionShoppingListFragmentToAddItemFragment()
                    )
            }

            is ShoppingListNavigation.ItemDetails -> {
                val item = nav.shoppingEntity
                findNavController().navigate(
                    ShoppingListFragmentDirections.actionShoppingListFragmentToEditItemFragment(
                        item.name,
                        item.quantity,
                        item.description,
                        item.isBought
                    )
                )
            }
        }
    }

    private fun onFilterPreferenceUpdated(preferences: FilterPreferences) {
        when (preferences.boughtFilter) {
            BoughtFilter.BOUGHT -> {
                binding.boughtChip.isChecked = true
                binding.notBoughtChip.isChecked = false
            }
            BoughtFilter.NOT_BOUGHT -> {
                binding.boughtChip.isChecked = false
                binding.notBoughtChip.isChecked = true
            }
            BoughtFilter.BOTH -> {
                binding.boughtChip.isChecked = true
                binding.notBoughtChip.isChecked = true
            }
        }

        when (preferences.sortOrder) {
            SortOrder.ASC -> binding.ascChip.isChecked = true
            SortOrder.DESC -> binding.dscChip.isChecked = true
        }
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
            listOf(R.id.boughtChip) -> BoughtFilter.BOUGHT
            listOf(R.id.notBoughtChip) -> BoughtFilter.NOT_BOUGHT
            // listOf(R.id.boughtChip, R.id.notBoughtChip), emptyList<Int>()
            else -> BoughtFilter.BOTH
        }
        Log.d("Remon", "onChipCheckedStateChanged: ${filter.name}")
        shoppingListViewModel.onBoughtFilterChanged(filter)
    }

    private fun renderShoppingList(state: State<List<ShoppingEntity>, QueryError>) {
        when (state) {
            is State.Error -> {
                binding.progressBar.invisible()
                showToast("An error occurred!")
            }

            State.Loading -> {
                binding.progressBar.visible()
            }

            is State.Success -> {
                binding.progressBar.invisible()
//                Log.d("Remon", "renderShoppingList: items: ${state.data}")
                shoppingListAdapter.submitList(state.data)
            }
        }
    }

    private fun onItemStateUpdated(state: State<UpdatedItem, UpdateError>) {
        when (state) {
            is State.Error -> showToast("couldn't update state. try again...")
            State.Loading -> {
                // TODO: progressbar on the checkbox?
            }
            is State.Success -> {
                val data = state.data
                val isBought = if (data.isBought) "bought" else "not bought"
                Log.d(TAG, "Updated item ${data.itemName} state to $isBought")
            }
        }
    }


    private val onItemClicked: (shoppingEntity: ShoppingEntity) -> Unit = { item ->
        shoppingListViewModel.onDetailsClicked(item)
    }

    private val onCheckStateChanged: (itemName: String, isChecked: Boolean) -> Unit =
        { name, isChecked ->
            shoppingListViewModel.changeBoughtStatus(name, isChecked)
        }

    private val onDeleteItemClicked: (shoppingEntity: ShoppingEntity) -> Unit = { item ->
        shoppingListViewModel.onDeleteClicked(item)
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.SearchMenuButton)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            shoppingListViewModel.searchQuery.value = it
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    companion object {
        private const val TAG = "ShoppingListFragment"
    }
}