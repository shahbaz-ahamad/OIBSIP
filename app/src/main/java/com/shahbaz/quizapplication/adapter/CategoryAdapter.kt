package com.shahbaz.quizapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.quizapplication.databinding.CategoryItemBinding
import com.shahbaz.quizapplication.datamodel.category.CategoryListItem
import com.shahbaz.quizapplication.util.Constant

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CatgoryViewholder>() {

    class CatgoryViewholder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: CategoryListItem?, background: Int) {
            currentItem?.let {
                binding.textTitle.text = it.name
                binding.linearLayoutBackground.setBackgroundColor(background)
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<CategoryListItem>() {
        override fun areItemsTheSame(
            oldItem: CategoryListItem,
            newItem: CategoryListItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryListItem,
            newItem: CategoryListItem,
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatgoryViewholder {
        return CatgoryViewholder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CatgoryViewholder, position: Int) {
        val currentItem = differ.currentList[position]
        val background = Constant.LIST_OF_COLOR.random()
        holder.bind(currentItem, background)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentItem.name)
        }
    }

    var onClick: ((String) -> Unit)? = null
}