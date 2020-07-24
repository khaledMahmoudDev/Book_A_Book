package com.example.bookabook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookabook.databinding.DownloadFilesElementRowBinding
import com.example.bookabook.model.FileModel

class BookFileAdapter(val bookFileListener : BookFileListener) : ListAdapter<FileModel, BookFileAdapter.BookFileAdapterViewHolder>(BookFileDiffUtill()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookFileAdapterViewHolder {
        return BookFileAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookFileAdapterViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file, bookFileListener)
    }



    class BookFileAdapterViewHolder private constructor(val binding: DownloadFilesElementRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: FileModel, bookFileListener : BookFileListener) {
            binding.rowFile = file
            binding.clickListener = bookFileListener
            binding.executePendingBindings()
        }
        companion object{
            fun from (parent :ViewGroup) : BookFileAdapterViewHolder
            {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding  = DownloadFilesElementRowBinding.inflate(layoutInflater, parent,false)
                return BookFileAdapterViewHolder(binding)

            }
        }

    }


}

class BookFileListener(val bookFileChange : (file : FileModel) ->Unit)
{
    fun onFileClick(file: FileModel) = bookFileChange(file)

}

class BookFileDiffUtill : DiffUtil.ItemCallback<FileModel>()
{
    override fun areItemsTheSame(oldItem: FileModel, newItem: FileModel): Boolean {
        return  oldItem.fileName == newItem.fileName
    }

    override fun areContentsTheSame(oldItem: FileModel, newItem: FileModel): Boolean {
        return oldItem == newItem
    }

}