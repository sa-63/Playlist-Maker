package com.project.playlistmaker.player_screen.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivityPlayerBinding
import com.project.playlistmaker.player_screen.domain.player_state.PlayerState
import com.project.playlistmaker.player_screen.ui.view_model.ActivityPlayerViewModel
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.use_cases.utilities.DataFormat

class ActivityPlayer : ComponentActivity() {

    companion object {
        const val TRACK_DTO_DATA = "track_dto_data"
        const val SEPARATE_DATE_STR_YEAR = 4
    }

    //ViewBinding
    private lateinit var binding: ActivityPlayerBinding

    //ViewModel
    private lateinit var activityPlayerViewModel: ActivityPlayerViewModel

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

        //ViewModelProvider & ViewModelObservers
        activityPlayerViewModel =
            ViewModelProvider(
                this,
                ActivityPlayerViewModel.getViewModelFactory()
            )[ActivityPlayerViewModel::class.java]

        activityPlayerViewModel.observePlayerState().observe(this) { playerState ->
            changePlayPauseBtnImage(playerState)
        }

        activityPlayerViewModel.observeDurationTime().observe(this) { durationTime ->
            binding.trackDurationCurrent.text = durationTime
        }

        //Listeners
        binding.playPauseBtn.setOnClickListener {
            activityPlayerViewModel.playbackControl(track.previewUrl)
        }

        binding.backBtnPlayer.setOnClickListener {
            onBackPressed()
        }
    }

    private fun changePlayPauseBtnImage(playerState: PlayerState) {
        if (playerState == PlayerState.STATE_PLAYING) {
            binding.playPauseBtn.setImageResource(R.drawable.pause_btn)
        } else {
            binding.playPauseBtn.setImageResource(R.drawable.ic_play)
        }
    }

    private fun fillContent() = with(binding) {
        Glide.with(albumCoverIv)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.size_8)))
            .placeholder(R.drawable.placeholder)
            .into(albumCoverIv)

        trackNameInPlayerTv.text = track.trackName
        artistNameTv.text = track.artistName
        binding.durationTotalTv.text = dataFormat.convertTimeToMnSs(track.trackTimeMillis)

        albumTv.text = track.collectionName
        yearTv.text = track.releaseDate.substring(0, SEPARATE_DATE_STR_YEAR)
        genreTv.text = track.primaryGenreName
        countryTv.text = track.country
    }

    override fun onBackPressed() {
        activityPlayerViewModel.releasePlayer()
        super.onBackPressed()
    }
}
