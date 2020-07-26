package com.example.bookabook.ui.favouriteBooks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.adapter.BookElementClickListener
import com.example.bookabook.adapter.ProfileBookListAdapter
import com.example.bookabook.databinding.FavouriteBooksFragmentBinding
import com.example.bookabook.ui.userAuthentication.logIn.LogInViewModel

class FavouriteBooksFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteBooksFragment()
    }

    private val viewModel: FavouriteBooksViewModel by activityViewModels()

    private val logInViewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FavouriteBooksFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        logInViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                Log.d(
                    "stateLogOut",
                    "observer " + logInViewModel.authenticationState.value.toString()
                )
                when (authenticationState) {
                    LogInViewModel.AuthenticationState.AUTHENTICATED -> {
                        viewModel.getBooksList()
                    }
                    LogInViewModel.AuthenticationState.UNAUTHENTICATED -> {
                        Log.d("stateLogOut", "observer entered unAuth")
                        findNavController()
                            .navigate(R.id.logInFragment)
                    }
                }
            })


        binding.myFavBooksViewModel = viewModel
        binding.myBooksList.adapter = ProfileBookListAdapter(BookElementClickListener {

            val action =
                FavouriteBooksFragmentDirections.actionFavouriteBooksToElementDetailsFragment(it)
            findNavController().navigate(action)
        })

        viewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                binding.favouriteBooksNotFound.visibility= View.VISIBLE
            }else
            {
                binding.favouriteBooksNotFound.visibility = View.GONE
            }
        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}