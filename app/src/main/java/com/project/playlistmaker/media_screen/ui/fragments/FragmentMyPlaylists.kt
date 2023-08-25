package com.project.playlistmaker.media_screen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.playlistmaker.databinding.FragmentMyPlaylistsBinding
import com.project.playlistmaker.media_screen.ui.view_models.FragmentMyPlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMyPlaylists : Fragment() {

    private val fragmentFavTracksView by viewModel<FragmentMyPlaylistsViewModel>()

    private lateinit var binding: FragmentMyPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FragmentMyPlaylists()
    }
}