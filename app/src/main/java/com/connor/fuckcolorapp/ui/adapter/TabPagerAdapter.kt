package com.connor.fuckcolorapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.connor.fuckcolorapp.ui.fragment.SystemAppFragment
import com.connor.fuckcolorapp.ui.fragment.UserAppFragment

class TabPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val fragments: List<Fragment> = ArrayList<Fragment>().apply {
        add(UserAppFragment())
        add(SystemAppFragment())
    }

    private val titles: List<String> = ArrayList<String>().apply {
        add("User")
        add("System")
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}