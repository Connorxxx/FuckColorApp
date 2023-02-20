package com.connor.fuckcolorapp.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.ui.fragment.AllAppsFragment
import com.connor.fuckcolorapp.ui.fragment.DisableFragment
import com.connor.fuckcolorapp.ui.fragment.SystemAppFragment
import com.connor.fuckcolorapp.ui.fragment.UserAppFragment

class TabPagerAdapter(private val ctx: Context, fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val fragments: List<Fragment> = ArrayList<Fragment>().apply {
        add(UserAppFragment())
        add(SystemAppFragment())
        add(AllAppsFragment())
        add(DisableFragment())
    }

    private val titles: List<String> = ArrayList<String>().apply {
        add(ctx.getString(R.string.user))
        add(ctx.getString(R.string.system))
        add(ctx.getString(R.string.all))
        add("Disable")
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}