package com.example.bookabook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.bookabook.databinding.BookHomeRowElementBinding
import com.example.bookabook.model.BooksModelRetreving

class HomeBookListAdapter :
    ListAdapter<BooksModelRetreving, HomeBookListAdapter.HomeBookListViewHolder>(HomeListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBookListViewHolder {
        return HomeBookListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeBookListViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class HomeBookListViewHolder private constructor(val binding: BookHomeRowElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BooksModelRetreving) {
            binding.bookElement = book
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HomeBookListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BookHomeRowElementBinding.inflate(layoutInflater, parent, false)
                return HomeBookListViewHolder(binding)
            }
        }

    }
}

class HomeListDiffUtil : DiffUtil.ItemCallback<BooksModelRetreving>() {
    override fun areItemsTheSame(oldItem: BooksModelRetreving, newItem: BooksModelRetreving): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BooksModelRetreving, newItem: BooksModelRetreving): Boolean {
        return oldItem == newItem
    }

}