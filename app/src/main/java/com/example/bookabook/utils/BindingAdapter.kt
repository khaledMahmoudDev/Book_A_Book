package com.example.bookabook.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bookabook.R
import com.example.bookabook.adapter.BookFileAdapter
import com.example.bookabook.adapter.HomeBookListAdapter
import com.example.bookabook.adapter.ProfileBookListAdapter
import com.example.bookabook.model.BooksModelRetreving
import com.example.bookabook.model.FileModel

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: Uri?) {
    imgUrl?.let { imgUri ->
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }

}

@BindingAdapter("imageString")
fun bindImageString(imgView: ImageView, imgString: String?) {
    imgString?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }

}


@BindingAdapter("app:hideIfFalse")
fun bindHideIfFalse(view: View, isFalse: Boolean) {
    view.visibility = if (!isFalse) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:hideIfTrue")
fun bindHideIfTrue(view: View, isFalse: Boolean) {
    view.visibility = if (isFalse) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:enableIfTrue")
fun enableIfTrue(view: View, isTrue: Boolean) {
    view.isEnabled = isTrue
}

@BindingAdapter("BookListData")
fun bindBookList(recyclerView: RecyclerView, data: List<BooksModelRetreving>?) {
    val adapter = recyclerView.adapter as HomeBookListAdapter
    adapter.submitList(data)
}

@BindingAdapter("BookListDataProfile")
fun bindBookListProfile(recyclerView: RecyclerView, data: List<BooksModelRetreving>?) {
    val adapter = recyclerView.adapter as ProfileBookListAdapter
    adapter.submitList(data)
}

@BindingAdapter("BookFileDataList")
fun BookFileDataList(recyclerView: RecyclerView, data: List<FileModel>?) {
    val adapter = recyclerView.adapter as BookFileAdapter
    adapter.submitList(data)
}

