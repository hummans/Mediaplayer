package com.mediaplayer.app


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mediaplayer.ui.AlbumsFragment
import com.mediaplayer.ui.ArtistsFragment
import com.mediaplayer.ui.PlaylistFragment
import com.mediaplayer.ui.home.HomeFragment

class ViewPagerAdapter(activity: BaseActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment.newInstance()
            }
            1 -> {
                AlbumsFragment.newInstance()
            }
            2 -> {
                PlaylistFragment.newInstance()
            }
            3 -> {
                ArtistsFragment.newInstance()
            }
            else -> HomeFragment.newInstance()
        }
    }
}