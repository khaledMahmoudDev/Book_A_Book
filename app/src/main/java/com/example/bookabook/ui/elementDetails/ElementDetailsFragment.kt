package com.example.bookabook.ui.elementDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookabook.R
import com.example.bookabook.databinding.ElementDetailsFragmentBinding

class ElementDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ElementDetailsFragment()
    }

    private lateinit var viewModel: ElementDetailsViewModel
    private lateinit var viewModelFactory: ElementDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ElementDetailsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val safeArgs: ElementDetailsFragmentArgs by navArgs()
        val book = safeArgs.clickedBook
        viewModelFactory = ElementDetailViewModelFactory(book)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ElementDetailsViewModel::class.java)
        binding.bookDetailViewModel = viewModel

        viewModel.navigateToLogIn.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                Toast.makeText(context, "please logIn to perform this action", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(R.id.logInFragment)
                viewModel.completeNavigateToLogIn()
            }
        })




        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.checkLogging()
    }

}