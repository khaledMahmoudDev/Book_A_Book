package com.example.bookabook.ui.myBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bookabook.adapter.ProfileBookListAdapter
import com.example.bookabook.databinding.MyBooksFragmentBinding

class MyBooksFragment : Fragment() {

    companion object {
        fun newInstance() = MyBooksFragment()
    }

    private val viewModel: MyBooksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = MyBooksFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        binding.myBooksViewModel = viewModel
        binding.myBooksList.adapter = ProfileBookListAdapter()


        return binding.root
    }



}