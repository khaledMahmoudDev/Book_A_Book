package com.example.bookabook.ui.userAuthentication.logIn

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.databinding.LogInFragmentBinding

class LogInFragment : Fragment() {

    companion object {
        fun newInstance() =
            LogInFragment()
    }

   private val viewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("stateLogOut", "ONCreatedLogIn")
        val binding = LogInFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.logInViewModel = viewModel


        viewModel.navigateToRegister.observe(viewLifecycleOwner, Observer {
            if (false != it)
            {
                this.findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
                viewModel.RegisterNavigationComplete()
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
            findNavController().popBackStack(R.id.homeFragment, false)
        }

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
                        findNavController().popBackStack()
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