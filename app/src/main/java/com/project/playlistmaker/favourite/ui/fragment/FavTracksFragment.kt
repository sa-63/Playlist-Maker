package com.project.playlistmaker.favourite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.FragmentFavTracksBinding
import com.project.playlistmaker.favourite.ui.model.FavTracksState
import com.project.playlistmaker.favourite.ui.viewmodel.FavTracksViewModel
import com.project.playlistmaker.playerscreen.ui.fragment.PlayerFragment
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.ui.adapter.TrackListAdapter
import com.project.playlistmaker.searchscreen.ui.view_holder.TrackListViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment(), TrackListViewHolder.TrackListClickListener {
    //ViewModel
    private val favTracksViewModel by viewModel<FavTracksViewModel>()

    //Binding
    private lateinit var binding: FragmentFavTracksBinding

    //For tracksRv
    private lateinit var favTracksAdapter: TrackListAdapter
    private var favTracksList = mutableListOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindAdapter()

        favTracksViewModel.getFavouriteTracks()

        favTracksViewModel.observeState().observe(viewLifecycleOwner) { favTrackState ->
            showContentBasedOnState(favTrackState)
        }
    }

    private fun showContentBasedOnState(state: FavTracksState) {
        when (state) {
            is FavTracksState.Content -> {
                favTracksList.clear()
                favTracksAdapter.notifyDataSetChanged()
                favTracksList.addAll(state.tracks as MutableList<Track>)
                binding.rvFavTracks.visibility = View.VISIBLE
                binding.llError.visibility = View.GONE
            }

            is FavTracksState.Empty -> {
                binding.rvFavTracks.visibility = View.GONE
                binding.llError.visibility = View.VISIBLE
            }
        }
    }

    //Open Player Activity
    //Open Player
    private fun openPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_PlayerFragment,
            PlayerFragment.createArgs(Gson().toJson(track)))
    }

    override fun onResume() {
        super.onResume()
        favTracksViewModel.getFavouriteTracks()
    }

    private fun bindAdapter() {
        favTracksAdapter = TrackListAdapter(favTracksList, this)
        binding.rvFavTracks.adapter = favTracksAdapter
    }

    companion object {
        fun newInstance() = FavTracksFragment()
    }

    override fun setTrackClickListener(track: Track) {
        favTracksViewModel.clickDebounce()
        openPlayer(track)
    }
}