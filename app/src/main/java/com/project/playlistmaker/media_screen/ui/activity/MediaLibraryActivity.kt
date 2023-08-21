package com.project.playlistmaker.media_screen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.project.playlistmaker.media_screen.ui.adapters.MediaViewPagerAdapter
import com.project.playlistmaker.media_screen.ui.fragments.FragmentFavTracks
import com.project.playlistmaker.media_screen.ui.fragments.FragmentMyPlaylists

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    private val fragmentsList = listOf(
        FragmentFavTracks.newInstance(),
        FragmentMyPlaylists.newInstance()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)
        //ViewBinding
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(
            this,
            fragmentsList
        )

        //TabMediator
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.fav_tracks)
                1 -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator.attach()

        //Listeners
        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}