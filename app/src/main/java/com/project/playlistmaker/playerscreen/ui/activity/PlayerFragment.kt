package com.project.playlistmaker.playerscreen.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.project.playlistmaker.databinding.ActivityPlayerBinding
import com.project.playlistmaker.playerscreen.ui.adapter.PlaylistsSmallAdapter
import com.project.playlistmaker.playerscreen.ui.model.PlayerState
import com.project.playlistmaker.playerscreen.ui.model.PlaylistsInPlayerState
import com.project.playlistmaker.playerscreen.ui.model.ToastState
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.ArgsTransfer
import com.project.playlistmaker.utils.DataFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : AppCompatActivity(), ArgsTransfer {

    //ViewBinding
    private lateinit var binding: ActivityPlayerBinding

    //Other
    private var bundleArgs: Bundle? = null
    private lateinit var track: Track
    private val dataFormat = DataFormat()

    private var playlistsAdapter = PlaylistsSmallAdapter {
        playerViewModel.getAllPlaylists(it)
    }

    //ViewModel
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        //Get track
//        track = Gson().fromJson(
//           getString(TRACK_ARG),
//            object : TypeToken<Track>() {}.type
//        )

        //Fill view with content
        fillContent()

        //PlayerState
        playerViewModel.observePlayerState().observe(this) { playerState ->
            changePlayPauseBtnImage(playerState)
            playerViewModel.observeCurrentDuration().observe(this) { duration ->
                binding.tvTrackDurationCurrent.text = duration
            }
        }

        //Favourite
        playerViewModel.checkIsFavourite(track.trackId)

        playerViewModel.observeFavourite().observe(this) { isFavorite ->
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
            playerViewModel.getAllPlaylists()
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
        playerViewModel.observeToastState().observe(this) { toastState ->
            if (toastState is ToastState.Show) {
                noPreviewUrlMessage(toastState.additionalMessage)
                playerViewModel.toastWasShown()
            }
        }

        //Playlists
        playerViewModel.observePlaylists().observe(this) {
            when (it) {
                is PlaylistsInPlayerState.DisplayPlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    binding.overlay.visibility = View.VISIBLE
                    displayPlaylists(it.myPlaylists)
                }

                is PlaylistsInPlayerState.HidePlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    binding.overlay.visibility = View.GONE
                }
            }
        }
    }

    private fun noPreviewUrlMessage(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_SHORT).show()
    }

    private fun displayPlaylists(myPlaylists: List<Playlist>) {
        playlistsAdapter.myPlaylists.clear()
        playlistsAdapter.myPlaylists.addAll(myPlaylists)
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
        tvYearInTable.text = track.releaseDate!!.substring(0, SEPARATE_DATE_STR_YEAR)
        tvGenreInTable.text = track.primaryGenreName
        tvCountryInTable.text = track.country
    }

    override fun onBackPressed() {
        playerViewModel.releasePlayer()
        super.onBackPressed()
    }

    private fun initBottomSheetRecyclerview() {
        binding.playlistsBottomSheetRecyclerview.adapter = playlistsAdapter
        binding.playlistsBottomSheetRecyclerview.layoutManager = LinearLayoutManager(this)
    }

    override fun getArgs(): Bundle? {
        return bundleArgs
    }

    override fun postArgs(args: Bundle?) {
        bundleArgs = args
    }

    private fun checkTransferredArgs() {
        if (getArgs() != null) {
            val namePlaylist = bundleArgs!!.getString(BUNDLE_ARGS)

            Toast.makeText(
                this,
                "${resources.getString(R.string.playlist)} $namePlaylist ${
                    resources.getString(R.string.created)
                }",
                Toast.LENGTH_LONG
            ).show()

            postArgs(null)
        }
    }

    companion object {
        fun createArgs(trackJson: String): Bundle = bundleOf(TRACK_ARG to trackJson)
        private const val TRACK_ARG = "TRACK_ARG"
        const val SEPARATE_DATE_STR_YEAR = 4
        const val BUNDLE_ARGS = "args"
    }
}