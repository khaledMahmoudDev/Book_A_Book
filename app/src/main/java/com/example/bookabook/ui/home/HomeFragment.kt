package com.example.bookabook.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.adapter.HomeBookListAdapter
import com.example.bookabook.databinding.HomeFragmentBinding
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private  val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = HomeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.homeViewModel  = viewModel
        binding.homeBookRecycler.adapter = HomeBookListAdapter()

        viewModel.navigateToLogIn.observe(viewLifecycleOwner, Observer {
            if (false != it)
            {
                //this.findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
                this.findNavController().navigate(R.id.action_homeFragment_to_addingBooksFragment)
                viewModel.navigateToLogInComplete()
            }
        })


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



//        extended_fab.setOnClickListener {
//
//            Toast.makeText(context,"FAB clicked", Toast.LENGTH_LONG).show()
//            this.findNavController().navigate(R.id.action_homeFragment_to_addingBooksFragment)
//        }
    }

}