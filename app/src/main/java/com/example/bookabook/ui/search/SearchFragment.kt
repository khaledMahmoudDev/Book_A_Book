package com.example.bookabook.ui.search

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.adapter.BookElementClickListener
import com.example.bookabook.adapter.ProfileBookListAdapter
import com.example.bookabook.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SearchFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        binding.searchViewModel = viewModel

        binding.myBooksList.adapter = ProfileBookListAdapter(BookElementClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToElementDetailsFragment(it)
            findNavController().navigate(action)

        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.fragmentSearchMenu)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchItem.expandActionView()
            val editext = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editext.hint = "Search for Books, Author"
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                        viewModel.setQuery(newText!!)

                    return true
                }

            })
        }

        super.onCreateOptionsMenu(menu, inflater)

    }


}