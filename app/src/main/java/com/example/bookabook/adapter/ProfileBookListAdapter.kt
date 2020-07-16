package com.example.bookabook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookabook.databinding.ProfileBookElementBinding
import com.example.bookabook.model.BooksModelRetreving

class ProfileBookListAdapter (val bookElementClickListener: BookElementClickListener):
    ListAdapter<BooksModelRetreving, ProfileBookListAdapter.ProfileBookListViewHolder>(
        ProfileBookDiffUtill()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileBookListViewHolder {
        return ProfileBookListViewHolder.fromProfileAdapter(parent)
    }

    override fun onBindViewHolder(holder: ProfileBookListViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book,bookElementClickListener)
    }

    class ProfileBookListViewHolder private constructor(val binding: ProfileBookElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            book: BooksModelRetreving?,
            bookElementClickListener: BookElementClickListener
        ) {
            binding.bookLinearElement = book
            binding.clickLiatener = bookElementClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun fromProfileAdapter(parent: ViewGroup): ProfileBookListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProfileBookElementBinding.inflate(layoutInflater, parent, false)
                return ProfileBookListViewHolder(binding)
            }
        }
    }


}


class ProfileBookDiffUtill : DiffUtil.ItemCallback<BooksModelRetreving>() {
    override fun areItemsTheSame(
        oldItem: BooksModelRetreving,
        newItem: BooksModelRetreving
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BooksModelRetreving,
        newItem: BooksModelRetreving
    ): Boolean {
        return oldItem == newItem
    }

}