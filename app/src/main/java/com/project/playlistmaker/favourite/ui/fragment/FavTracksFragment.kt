package com.project.playlistmaker.favourite.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.playlistmaker.databinding.FragmentFavTracksBinding
import com.project.playlistmaker.favourite.ui.model.FavTracksState
import com.project.playlistmaker.favourite.ui.viewmodel.FavTracksViewModel
import com.project.playlistmaker.playerscreen.ui.activity.PlayerActivity
import com.project.playlistmaker.playlist.ui.model.TrackPlr
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.ui.adapter.TrackListAdapter
import com.project.playlistmaker.searchscreen.ui.viewholder.TrackListViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment(), TrackListViewHolder.TrackListClickListener {

    private val favTracksViewModel by viewModel<FavTracksViewModel>()

    private lateinit var binding: FragmentFavTracksBinding

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

    private fun openPlayer(track: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra(INTENT_TRACK, TrackPlr.mappingTrack(track))
        requireContext().startActivity(intent)
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
        const val INTENT_TRACK = "dataTrack"
    }

    override fun setTrackClickListener(track: Track) {
        favTracksViewModel.clickDebounce()
        openPlayer(track)
    }
}