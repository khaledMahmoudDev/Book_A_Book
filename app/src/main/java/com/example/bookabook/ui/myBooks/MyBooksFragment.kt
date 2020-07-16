package com.example.bookabook.ui.myBooks

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
import com.example.bookabook.databinding.MyBooksFragmentBinding
import com.example.bookabook.ui.userAuthentication.logIn.LogInViewModel

class MyBooksFragment : Fragment() {

    companion object {
        fun newInstance() = MyBooksFragment()
    }

    private val viewModel: MyBooksViewModel by activityViewModels()

    private val logInViewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = MyBooksFragmentBinding.inflate(layoutInflater)
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

        binding.myBooksViewModel = viewModel
        binding.myBooksList.adapter = ProfileBookListAdapter(BookElementClickListener {
            val action = MyBooksFragmentDirections.actionMyBooksToElementDetailsFragment(it)
            findNavController().navigate(action)

        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}