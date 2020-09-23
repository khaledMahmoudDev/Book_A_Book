package com.example.bookabook.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.adapter.BookElementClickListener
import com.example.bookabook.adapter.HomeBookListAdapter
import com.example.bookabook.databinding.HomeFragmentBinding
import com.example.bookabook.utils.createChannel


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.homeViewModel = viewModel

        setHasOptionsMenu(true)

        createChannel(
            requireActivity().getString(R.string.newBookChannelId),
            "New Book",
            "newly Added Books",
            requireActivity()
        )

        binding.homeBookRecycler.adapter = HomeBookListAdapter(BookElementClickListener {
            Toast.makeText(context, it.bookTitle, Toast.LENGTH_LONG).show()
            val action = HomeFragmentDirections.actionHomeFragmentToElementDetailsFragment(it)
            findNavController().navigate(action)


        })

        viewModel.navigateToLogIn.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                //this.findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
                this.findNavController().navigate(R.id.action_homeFragment_to_addingBooksFragment)
                viewModel.navigateToLogInComplete()
            }
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeSearch -> {
                findNavController().navigate(R.id.searchFragment)
            }

        }

        return super.onOptionsItemSelected(item)
    }


}