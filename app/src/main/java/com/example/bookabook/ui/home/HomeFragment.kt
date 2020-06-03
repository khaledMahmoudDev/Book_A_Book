package com.example.bookabook.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bookabook.R
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        homeFragmentChipGroup.check(R.id.homeGroupChipAll)
        val checkedChipId = homeFragmentChipGroup.checkedChipId // Will return View.NO_ID if singleSelection = false
        val checkedChipIds = homeFragmentChipGroup.checkedChipIds
        homeFragmentChipGroup.setOnCheckedChangeListener { group, checkedId ->
            Toast.makeText(context,"clicked is $checkedId", Toast.LENGTH_LONG).show()
            // Handle child Chip checked/unchecked
        }
        extended_fab.setOnClickListener {

            Toast.makeText(context,"FAB clicked", Toast.LENGTH_LONG).show()
        }
    }

}