package com.example.bookabook.ui.favouriteBooks

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookabook.R

class FavouriteBooksFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteBooksFragment()
    }

    private lateinit var viewModel: FavouriteBooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourite_books_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouriteBooksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}