package com.project.playlistmaker.playerscreen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivityPlayerBinding
import com.project.playlistmaker.playerscreen.ui.model.playerstate.PlayerState
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.DataFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityPlayerBinding

    //ViewModel
    private val playerViewModel by viewModel<PlayerViewModel>()

    //Other
    private lateinit var track: Track
    private val dataFormat = DataFormat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        //ViewBinding
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(TRACK_DTO_DATA) as Track

        fillContent()

        playerViewModel.observePlayerState().observe(this) { playerState ->
            changePlayPauseBtnImage(playerState)
            playerViewModel.observeCurrentDuration().observe(this) { duration ->
                binding.tvTrackDurationCurrent.text = duration
            }
        }

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

    override fun onBackPressed() {
        playerViewModel.releasePlayer()
        super.onBackPressed()
    }

    companion object {
        const val TRACK_DTO_DATA = "track_dto_data"
        const val SEPARATE_DATE_STR_YEAR = 4
    }
}
