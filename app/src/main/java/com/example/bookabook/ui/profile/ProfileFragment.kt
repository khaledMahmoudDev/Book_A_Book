package com.example.bookabook.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.bookabook.R
import com.example.bookabook.adapter.ProfileFragmentPagerAdapter
import com.example.bookabook.databinding.ProfileFragmentBinding
import com.example.bookabook.ui.userAuthentication.logIn.LogInViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ProfileFragment : Fragment() {
    private lateinit var profileFragmentPagerAdapter: ProfileFragmentPagerAdapter


    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by activityViewModels()
    private val logInViewModel: LogInViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("stateLogOut", "ONViewCreated")
        viewModel.logOutbtn.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                logInViewModel.signOut()
                viewModel.completeLogOut()

            }
        })

        logInViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                Log.d(
                    "stateLogOut",
                    "observer " + logInViewModel.authenticationState.value.toString()
                )
                when (authenticationState) {
                    LogInViewModel.AuthenticationState.AUTHENTICATED -> {
                        showWelcomeMessage()
                        viewModel.getUserNow()
                    }
                    LogInViewModel.AuthenticationState.UNAUTHENTICATED -> {
                        Log.d("stateLogOut", "observer entered unAuth")
                        findNavController()
                            .navigate(R.id.logInFragment)
                    }
                }
            })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProfileFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.profileViewModel = viewModel




        return binding.root
    }



    private fun showWelcomeMessage() {
        Toast.makeText(context, "welcome", Toast.LENGTH_LONG).show()
    }
}