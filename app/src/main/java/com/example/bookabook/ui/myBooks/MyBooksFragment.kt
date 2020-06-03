package com.example.bookabook.ui.myBooks

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookabook.R

class MyBooksFragment : Fragment() {

    companion object {
        fun newInstance() = MyBooksFragment()
    }

    private lateinit var viewModel: MyBooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_books_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyBooksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}