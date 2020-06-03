package com.example.bookabook.ui.addingBook

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookabook.R

class AddingBooksFragment : Fragment() {

    companion object {
        fun newInstance() = AddingBooksFragment()
    }

    private lateinit var viewModel: AddingBooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adding_books_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddingBooksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}