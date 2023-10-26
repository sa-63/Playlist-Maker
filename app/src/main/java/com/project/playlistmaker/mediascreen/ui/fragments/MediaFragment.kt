package com.project.playlistmaker.mediascreen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.FragmentMediaBinding
import com.project.playlistmaker.favourite.ui.fragment.FavTracksFragment
import com.project.playlistmaker.mediascreen.ui.adapters.MediaViewPagerAdapter
import com.project.playlistmaker.playlist.ui.fragments.PlaylistsFragment

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    private val fragmentsList = listOf(
        FavTracksFragment.newInstance(),
        PlaylistsFragment.newInstance()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewPager Adapter
        binding.viewPager.adapter = MediaViewPagerAdapter(
            childFragmentManager,
            lifecycle,
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }
}