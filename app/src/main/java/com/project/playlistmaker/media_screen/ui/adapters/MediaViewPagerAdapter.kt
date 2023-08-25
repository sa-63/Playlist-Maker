package com.project.playlistmaker.media_screen.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.playlistmaker.media_screen.ui.fragments.FragmentFavTracks
import com.project.playlistmaker.media_screen.ui.fragments.FragmentMyPlaylists

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
            0 -> FragmentFavTracks.newInstance()
            else -> FragmentMyPlaylists.newInstance()
        }
    }
}