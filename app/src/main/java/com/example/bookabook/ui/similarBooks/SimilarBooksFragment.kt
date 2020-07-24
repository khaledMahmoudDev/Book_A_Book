package com.example.bookabook.ui.similarBooks

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookabook.R
import com.example.bookabook.adapter.BookElementClickListener
import com.example.bookabook.adapter.ProfileBookListAdapter
import com.example.bookabook.databinding.SimilarBooksFragmentBinding
import com.example.bookabook.ui.elementDetails.ElementDetailsFragmentArgs
import com.example.bookabook.ui.elementDetails.ElementDetailsViewModel
import com.example.bookabook.ui.myBooks.MyBooksFragmentDirections

class SimilarBooksFragment : Fragment() {

    companion object {
        fun newInstance() = SimilarBooksFragment()
    }

    private lateinit var viewModel: SimilarBooksViewModel
    private lateinit var viewModelFactory : SimilarBooksViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = SimilarBooksFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val safeArgs: ElementDetailsFragmentArgs by navArgs()
        val book = safeArgs.clickedBook

        viewModelFactory = SimilarBooksViewModelFactory(book)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SimilarBooksViewModel::class.java)
        binding.viewModel = viewModel

        binding.myBooksList.adapter = ProfileBookListAdapter(BookElementClickListener {

            val action = SimilarBooksFragmentDirections.actionSimilarBooksFragmentToElementDetailsFragment(it)
            findNavController().navigate(action)

        })





        Toast.makeText(context, book.bookTitle,Toast.LENGTH_LONG).show()
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}