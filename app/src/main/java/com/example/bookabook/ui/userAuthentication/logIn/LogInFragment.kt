package com.example.bookabook.ui.userAuthentication.logIn

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.databinding.LogInFragmentBinding

class LogInFragment : Fragment() {

    companion object {
        fun newInstance() =
            LogInFragment()
    }

    private lateinit var viewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LogInFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this).get(LogInViewModel::class.java)
        binding.logInViewModel = viewModel

        viewModel.navigateToRegister.observe(viewLifecycleOwner, Observer {
            if (false != it)
            {
                this.findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
                viewModel.RegisterNavigationComplete()
            }
        })
        return  binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}