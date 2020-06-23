package com.example.bookabook.ui.userAuthentication.logIn

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        viewModel.logInState.observe(viewLifecycleOwner, Observer {
            if (null != it)
            {
                var toastMessage: String = ""
                when(it)
                {
                    LogInStateState.EmailNotValid ->
                    {
                        toastMessage = "Please Enter Valid Email"
                        binding.editTextTextEmail.requestFocus()
                    }
                    LogInStateState.PasswordNotValid ->
                    {
                        toastMessage = "Please Enter Valid Password"
                        binding.editTextTextPassword.requestFocus()
                    }
                    LogInStateState.EmailIsNotVerified ->
                    {
                        toastMessage = "Email is Not verified please check your mail inbox"
                    }
                    LogInStateState.EmailOrPasswordIsNotCorrect ->
                    {
                        toastMessage = "Password Or Email Is not Valid"
                    }
                    LogInStateState.FAiledToLogIn ->
                    {
                        toastMessage = "Failed To LogIn, try again"
                    }
                    LogInStateState.LoggedIn->
                    {
                        toastMessage = "LoggedIn"
                    }
                    else -> {
                        toastMessage = "Unexpected Error, Try Again"
                    }
                }
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }
        })
        return  binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}