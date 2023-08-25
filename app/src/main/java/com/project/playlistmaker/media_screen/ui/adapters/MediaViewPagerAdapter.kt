package com.project.playlistmaker.media_screen.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.playlistmaker.media_screen.ui.activity.MediaLibraryActivity

class MediaViewPagerAdapter(
    libraryActivity: MediaLibraryActivity,
    private val list: List<Fragment>
) : FragmentStateAdapter(libraryActivity) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}