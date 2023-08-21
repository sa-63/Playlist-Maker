package com.project.playlistmaker.media_screen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.playlistmaker.databinding.FragmentFavTracksBinding
import com.project.playlistmaker.media_screen.ui.view_models.FragmentFavTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavTracks : Fragment() {

    private val fragmentFavTracksView by viewModel<FragmentFavTracksViewModel>()

    private lateinit var binding: FragmentFavTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentFavTracks()
    }
}