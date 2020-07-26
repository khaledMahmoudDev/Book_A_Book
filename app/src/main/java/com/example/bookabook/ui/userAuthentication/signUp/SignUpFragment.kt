package com.example.bookabook.ui.userAuthentication.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SignUpFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        binding.signUpViewModel = viewModel

        viewModel.registerState.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                var toastMessage: String = ""
                when (it) {
                    SignUpStateState.UserNameNotValid -> {
                        toastMessage = "Please Enter Valid User Name"
                        binding.editTextTextUserName.requestFocus()

                    }
                    SignUpStateState.EmailNotValid -> {
                        toastMessage = "Please Enter Valid Email"
                        binding.editTextTextUseEmail.requestFocus()

                    }
                    SignUpStateState.PasswordNotValid -> {
                        toastMessage = "Please Enter Valid Password"
                        binding.editTextTextUsePassword.requestFocus()
                    }
                    SignUpStateState.PasswordOrEmailError -> {
                        toastMessage = "Password Or Email Is not Valid"
                    }
                    SignUpStateState.FailedToRegister -> {
                        toastMessage = "Failed To Register, try again"
                    }
                    SignUpStateState.Registered -> {
                        toastMessage = "Please check your mail inbox for verification code"
                        this.findNavController()
                            .navigate(R.id.action_signUpFragment_to_logInFragment)
                    }
                    else -> {
                        toastMessage = "Unexpected Error, Try Again"
                    }

                }
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()

            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}