package com.example.bookabook.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.adapter.ProfileFragmentPagerAdapter
import com.example.bookabook.databinding.ProfileFragmentBinding
import com.example.bookabook.ui.userAuthentication.logIn.LogInViewModel
import com.google.android.material.tabs.TabLayoutMediator


class ProfileFragment : Fragment() {
    private lateinit var profileFragmentPagerAdapter: ProfileFragmentPagerAdapter


    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val logInViewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProfileFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        logInViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
                    LogInViewModel.AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
                    LogInViewModel.AuthenticationState.UNAUTHENTICATED -> this.findNavController()
                        .navigate(R.id.logInFragment)
                }
            })
        profileFragmentPagerAdapter = ProfileFragmentPagerAdapter(this)
        binding.profileViewPager.adapter = profileFragmentPagerAdapter

        val viewPager = binding.profileViewPager
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    "My Books"
                }
                else -> {
                    "Fav Books"
                }
            }

        }.attach()

        return binding.root
    }

    private fun showWelcomeMessage() {
        Toast.makeText(context, "welcome", Toast.LENGTH_LONG).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }


}