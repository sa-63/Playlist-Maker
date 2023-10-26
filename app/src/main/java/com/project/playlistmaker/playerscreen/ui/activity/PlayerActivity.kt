package com.project.playlistmaker.playerscreen.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivityPlayerBinding
import com.project.playlistmaker.playerscreen.ui.adapter.PlaylistPlayerAdapter
import com.project.playlistmaker.playerscreen.ui.model.PlayerState
import com.project.playlistmaker.playerscreen.ui.model.ToastState
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb
import com.project.playlistmaker.playlist.domain.models.entity.Playlist
import com.project.playlistmaker.playlist.ui.fragments.NewPlaylistFragment
import com.project.playlistmaker.playlist.ui.model.TrackPlr
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.ArgsTransfer
import com.project.playlistmaker.utils.DataFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity(), ArgsTransfer {

    //ViewBinding
    private lateinit var binding: ActivityPlayerBinding

    //Other
    private var bundleArgs: Bundle? = null
    private lateinit var track: Track
    private val dataFormat = DataFormat()
    private lateinit var dataTrack: TrackPlr
    private val playlists = ArrayList<Playlist>()
    private lateinit var playlistAdapter: PlaylistPlayerAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var onClickPlaylistCallback: PlaylistPlayerAdapter.PlaylistClickListener

    //ViewModel
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Track
        dataTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("dataTrack", TrackPlr::class.java) ?: TrackPlr()
        } else {
            intent.getParcelableExtra("dataTrack") ?: TrackPlr()
        }
        track = TrackPlr.mappingTrack(dataTrack)

        fillContent()

        //PlayerState
        playerViewModel.observePlayerState().observe(this) { playerState ->
            changePlayPauseBtnImage(playerState)
            playerViewModel.observeCurrentDuration().observe(this) { duration ->
                binding.tvTrackDurationCurrent.text = duration
            }
        }

        //Playlist
        playerViewModel.getEmptyPlaylistLiveData().observe(this) {
            renderPlaylist(it)
        }

        playerViewModel.getAddPlaylistLivaData().observe(this) {
            renderAddTrackInPlaylist(it)
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
            playerViewModel.releasePlayer()
            finish()
        }

        binding.btnAddToFav.setOnClickListener {
            playerViewModel.onFavouriteClicked(track)
        }

        binding.btnAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            playerViewModel.getAllPlaylists()
        }

        binding.btnNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            supportFragmentManager.commit {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                replace(R.id.fragmentContainerPlayer, NewPlaylistFragment.newInstance(true))
                addToBackStack("player")
                setReorderingAllowed(true)
            }
        }

        onClickPlaylistCallback = object : PlaylistPlayerAdapter.PlaylistClickListener {
            override fun onClickView(playlist: Playlist) {
                playerViewModel.addTrackInPlaylist(dataTrack, playlist)
            }
        }

        //Toast
        playerViewModel.observeToastState().observe(this) { toastState ->
            if (toastState is ToastState.Show) {
                noPreviewUrlMessage(toastState.additionalMessage)
                playerViewModel.toastWasShown()
            }
        }

        //BottomSheet
        playlistAdapter =
            PlaylistPlayerAdapter(playlists, { lifecycleScope }, onClickPlaylistCallback)
        binding.playlistsBottomSheetRecyclerview.adapter = playlistAdapter
        binding.playlistsBottomSheetRecyclerview.layoutManager = LinearLayoutManager(this)

        initBottomSheetRecyclerview()

        val bottomSheetContainer = binding.bottomSheetLinear
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }
        })
    }

    private fun noPreviewUrlMessage(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_SHORT).show()
    }

    private fun renderPlaylist(state: EmptyStatePlaylist) {
        when (state) {
            is EmptyStatePlaylist.EmptyPlaylist -> {
                binding.playlistsBottomSheetRecyclerview.isVisible = false
            }

            is EmptyStatePlaylist.NotEmptyPlaylist -> {
                playlists.clear()
                playlists.addAll(state.playlist)
                playlistAdapter.notifyDataSetChanged()
                binding.playlistsBottomSheetRecyclerview.isVisible = true
            }
        }
    }

    private fun renderAddTrackInPlaylist(state: StateAddDb) {
        when (state) {
            is StateAddDb.Match -> {
                playerViewModel.showToast("${getString(R.string.track_is_in_pl_already)} ${state.namePlaylist}")
                playerViewModel.setStateNoDataPlaylistLivaData()
            }

            is StateAddDb.Error -> {
                Log.e("ErrorAddBd", getString(R.string.error_add_playlist))
            }

            is StateAddDb.NoError -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                playerViewModel.showToast("${getString(R.string.track_added_to_pl)} ${state.namePlaylist}")
                playerViewModel.setStateNoDataPlaylistLivaData()
            }

            is StateAddDb.NoData -> {
            }
        }
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
        tvYearInTable.text = track.releaseDate?.substring(0, SEPARATE_DATE_STR_YEAR)
        tvGenreInTable.text = track.primaryGenreName
        tvCountryInTable.text = track.country
    }

    override fun onBackPressed() {
        playerViewModel.releasePlayer()
        super.onBackPressed()
    }

    private fun initBottomSheetRecyclerview() {
        binding.playlistsBottomSheetRecyclerview.adapter = playlistAdapter
        binding.playlistsBottomSheetRecyclerview.layoutManager = LinearLayoutManager(this)
    }

    override fun getArgs(): Bundle? {
        return bundleArgs
    }

    override fun postArgs(args: Bundle?) {
        bundleArgs = args
    }

    companion object {
        const val SEPARATE_DATE_STR_YEAR = 4
        const val BUNDLE_ARGS = "args"
    }
}