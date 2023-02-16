package com.connor.fuckcolorapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    fragments: List<Fragment>,
    titles: List<String>
)  : FragmentStateAdapter(fm, lifecycle) {
    private val mFragments: List<Fragment> = fragments
    private val mTitles: List<String> = titles

    override fun getItemCount() = mFragments.size

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

    fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }
}