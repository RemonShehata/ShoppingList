package com.example.shoppinglist.features.shoppinglist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.databinding.ShoppingItemBinding

class ShoppingListAdapter(
    private val onItemClicked: (item: ShoppingEntity) -> Unit,
    private val onCheckStateChanged: (itemName: String, isChecked: Boolean) -> Unit,
    private val onDeleteItemClicked: (shoppingEntity: ShoppingEntity) -> Unit
) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private val differ: AsyncListDiffer<ShoppingEntity> =
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

            deleteButton.setOnClickListener {
                onDeleteItemClicked(currentItem)
            }

            itemName.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("Remon", "setOnCheckedChangeListener: $isChecked")
                onCheckStateChanged(currentItem.name, isChecked)
            }

            itemName.text = currentItem.name
            itemQuantity.text = currentItem.quantity.toString()
            itemName.isChecked = currentItem.isBought
        }

    }

    override fun getItemCount(): Int = differ.currentList.count()

    fun submitList(readings: List<ShoppingEntity>) {
        differ.submitList(readings)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShoppingEntity>() {
            override fun areItemsTheSame(
                oldItem: ShoppingEntity,
                newItem: ShoppingEntity
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: ShoppingEntity,
                newItem: ShoppingEntity
            ): Boolean = oldItem == newItem
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].hashCode().toLong()
    }

    inner class ShoppingListViewHolder(val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
