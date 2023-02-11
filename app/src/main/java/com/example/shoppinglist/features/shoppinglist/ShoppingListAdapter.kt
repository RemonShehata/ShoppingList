package com.example.shoppinglist.features.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.data.local.models.ShoppingItemEntity
import com.example.shoppinglist.databinding.ShoppingItemBinding

class ShoppingListAdapter(
    private val onItemClicked: (item: ShoppingItemEntity) -> Unit
) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private val differ: AsyncListDiffer<ShoppingItemEntity> =
        AsyncListDiffer(this, DIFF_CALLBACK)

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
        val currentItem = differ.currentList[position]

        with(holder.binding) {
            root.setOnClickListener {
                onItemClicked(currentItem)
            }

            itemName.text = currentItem.name
            itemQuantity.text = currentItem.quantity.toString()
        }

    }

    override fun getItemCount(): Int = differ.currentList.count()

    fun submitList(readings: List<ShoppingItemEntity>) {
        differ.submitList(readings)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShoppingItemEntity>() {
            override fun areItemsTheSame(
                oldItem: ShoppingItemEntity,
                newItem: ShoppingItemEntity
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: ShoppingItemEntity,
                newItem: ShoppingItemEntity
            ): Boolean = oldItem == newItem
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ShoppingListViewHolder(val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
