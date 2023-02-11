package com.example.shoppinglist.features.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.databinding.ShoppingItemBinding

// Diff utils doesn't work with adding new data, it move the scroll position to the top.
class ShoppingListAdapter(
    private val onItemClicked: (item: ShoppingItemEntity) -> Unit,
    private var list: List<ShoppingItemEntity>
) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        return ShoppingListViewHolder(
            ShoppingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val currentItem = list[position]

        with(holder.binding) {
            root.setOnClickListener {
                onItemClicked(currentItem)
            }

            itemName.text = currentItem.name
            itemQuantity.text = currentItem.quantity.toString()
        }

    }

    override fun getItemCount(): Int = list.size

    fun setItems(newList: List<ShoppingItemEntity>) {
        val oldSize = list.size
        this.list = newList
//        notifyItemRangeChanged(oldSize, newList.size)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ShoppingListViewHolder(val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
