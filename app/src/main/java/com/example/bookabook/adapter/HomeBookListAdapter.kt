package com.example.bookabook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookabook.R
import com.example.bookabook.databinding.BookHomeRowElementBinding
import com.example.bookabook.model.BooksModel

class HomeBookListAdapter :
    ListAdapter<BooksModel, HomeBookListAdapter.HomeBookListViewHolder>(HomeListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBookListViewHolder {
        return HomeBookListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeBookListViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class HomeBookListViewHolder private constructor(val binding: BookHomeRowElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BooksModel) {
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

class HomeListDiffUtil : DiffUtil.ItemCallback<BooksModel>() {
    override fun areItemsTheSame(oldItem: BooksModel, newItem: BooksModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BooksModel, newItem: BooksModel): Boolean {
        return oldItem == newItem
    }

}