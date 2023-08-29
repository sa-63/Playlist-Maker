package com.project.playlistmaker.mediascreen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.playlistmaker.databinding.FragmentFavTracksBinding
import com.project.playlistmaker.mediascreen.ui.viewmodels.FavTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment() {

    private val fragmentFavTracksView by viewModel<FavTracksViewModel>()

    private lateinit var binding: FragmentFavTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FavTracksFragment()
    }
}