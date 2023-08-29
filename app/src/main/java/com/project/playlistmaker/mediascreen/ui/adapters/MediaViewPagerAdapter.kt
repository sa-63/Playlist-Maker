package com.project.playlistmaker.mediascreen.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.playlistmaker.mediascreen.ui.fragments.FavTracksFragment
import com.project.playlistmaker.mediascreen.ui.fragments.MyPlaylistsFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val list: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavTracksFragment.newInstance()
            else -> MyPlaylistsFragment.newInstance()
        }
    }
}