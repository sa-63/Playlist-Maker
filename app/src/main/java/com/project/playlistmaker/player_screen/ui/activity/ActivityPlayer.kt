package com.project.playlistmaker.player_screen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivityPlayerBinding
import com.project.playlistmaker.player_screen.ui.model.player_state.PlayerState
import com.project.playlistmaker.player_screen.ui.view_model.ActivityPlayerViewModel
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.utils.DataFormat

class ActivityPlayer : AppCompatActivity() {

    companion object {
        const val TRACK_DTO_DATA = "track_dto_data"
        const val SEPARATE_DATE_STR_YEAR = 4
    }

    //ViewBinding
    private lateinit var binding: ActivityPlayerBinding

    //ViewModel
    private var activityPlayerViewModel: ActivityPlayerViewModel? = null

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

        //ViewModelProvider & ViewModelObserver
        activityPlayerViewModel =
            ViewModelProvider(
                this,
                ActivityPlayerViewModel.getViewModelFactory()
            )[ActivityPlayerViewModel::class.java]

        activityPlayerViewModel!!.observePlayerState().observe(this) { playerState ->
            changePlayPauseBtnImage(playerState)
            binding.tvTrackDurationCurrent.text =
                activityPlayerViewModel!!.getCurrentTrackDuration()
        }

        //Listeners
        binding.btnPlayPause.setOnClickListener {
            activityPlayerViewModel!!.playbackControl(track.previewUrl)
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
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
        activityPlayerViewModel!!.releasePlayer()
        super.onBackPressed()
    }
}
