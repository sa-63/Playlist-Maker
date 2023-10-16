package com.project.playlistmaker.playerscreen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.playlistmaker.R
import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.databinding.FragmentPlayerBinding
import com.project.playlistmaker.playerscreen.ui.adapter.PlaylistsSmallAdapter
import com.project.playlistmaker.playerscreen.ui.model.PlayerState
import com.project.playlistmaker.playerscreen.ui.model.PlaylistsInPlayerState
import com.project.playlistmaker.playerscreen.ui.model.ToastState
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.DataFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    //ViewBinding
    private lateinit var binding: FragmentPlayerBinding

    //Other
    private lateinit var track: Track
    private val dataFormat = DataFormat()

    private var playlistsAdapter = PlaylistsSmallAdapter {
        playerViewModel.onPlaylistClicked(it)
    }

    //ViewModel
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Get track
        track = Gson().fromJson(
            requireArguments().getString(TRACK_ARG),
            object : TypeToken<Track>() {}.type
        )

        //Fill view with content
        fillContent()

        //PlayerState
        playerViewModel.observePlayerState().observe(requireActivity()) { playerState ->
            changePlayPauseBtnImage(playerState)
            playerViewModel.observeCurrentDuration().observe(requireActivity()) { duration ->
                binding.tvTrackDurationCurrent.text = duration
            }
        }

        //Favourite
        playerViewModel.checkIsFavourite(track.trackId)

        playerViewModel.observeFavourite().observe(requireActivity()) { isFavorite ->
            binding.btnAddToFav.setImageResource(
                if (isFavorite) R.drawable.ic_like_on else R.drawable.ic_like
            )
        }

        //Listeners
        binding.btnPlayPause.setOnClickListener {
            playerViewModel.playbackControl(track.previewUrl)
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddToFav.setOnClickListener {
            playerViewModel.onFavouriteClicked(track)
        }

        binding.btnAddToPlaylist.setOnClickListener {
            playerViewModel.addToPlaylistClicked()
        }

        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_PlayerFragment_to_newPlaylistFragment)
        }

        //BottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLinear).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        //BottomSheetRv
        initBottomSheetRecyclerview()

        //Toast
        playerViewModel.observeToastState().observe(viewLifecycleOwner) { toastState ->
            if (toastState is ToastState.Show) {
                noPreviewUrlMessage(toastState.additionalMessage)
                playerViewModel.toastWasShown()
            }
        }

        //Playlists
        playerViewModel.observePlaylists().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsInPlayerState.DisplayPlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    binding.overlay.visibility = View.VISIBLE
                    displayPlaylists(it.playlists)
                }

                is PlaylistsInPlayerState.HidePlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    binding.overlay.visibility = View.GONE
                }
            }
        }
    }

    private fun noPreviewUrlMessage(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_SHORT).show()
    }

    private fun displayPlaylists(playlists: List<Playlist>) {
        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    private fun changePlayPauseBtnImage(playerState: PlayerState) {
        if (playerState == PlayerState.STATE_PLAYING) {
            binding.btnPlayPause.setImageResource(R.drawable.pause_btn)
        } else {
            binding.btnPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun fillContent() = with(binding) {
        Glide.with(ivAlbumCover)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.size_8)))
            .placeholder(R.drawable.placeholder)
            .into(ivAlbumCover)

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        binding.tvDurationTotal.text = dataFormat.convertTimeToMnSs(track.trackTimeMillis)

        tvAlbumInTable.text = track.collectionName
        tvYearInTable.text = track.releaseDate.substring(0, SEPARATE_DATE_STR_YEAR)
        tvGenreInTable.text = track.primaryGenreName
        tvCountryInTable.text = track.country
    }

    private fun onBackPressed() {
        playerViewModel.releasePlayer()
        super.requireActivity().onBackPressed()
    }

    private fun initBottomSheetRecyclerview() {
        binding.playlistsBottomSheetRecyclerview.adapter = playlistsAdapter
        binding.playlistsBottomSheetRecyclerview.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        fun createArgs(trackJson: String): Bundle = bundleOf(TRACK_ARG to trackJson)
        private const val TRACK_ARG = "TRACK_ARG"
        const val SEPARATE_DATE_STR_YEAR = 4
    }
}