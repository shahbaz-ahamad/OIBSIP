package com.shahbaz.unitconverter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.unitconverter.databinding.ConverterItemLayoutBinding
import com.shahbaz.unitconverter.datamodel.UnitConverterItem

class ConverterItemAdapter : RecyclerView.Adapter<ConverterItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: ConverterItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: UnitConverterItem?) {
            currentItem?.let {
                binding.image.setImageResource(it.image)
                binding.textView.text = it.name
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<UnitConverterItem>() {
        override fun areItemsTheSame(
            oldItem: UnitConverterItem,
            newItem: UnitConverterItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UnitConverterItem,
            newItem: UnitConverterItem,
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ConverterItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentItem)
        }
    }

    var onClick: ((UnitConverterItem) -> Unit)? = null
}