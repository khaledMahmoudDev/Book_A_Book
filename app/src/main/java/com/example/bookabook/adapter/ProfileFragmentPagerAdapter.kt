package com.example.bookabook.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookabook.ui.favouriteBooks.FavouriteBooksFragment
import com.example.bookabook.ui.myBooks.MyBooksFragment

class ProfileFragmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyBooksFragment()
            else -> FavouriteBooksFragment()
        }
    }
}