package com.project.playlistmaker.player_screen.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.player_screen.domain.player_state.PlayerState
import com.project.playlistmaker.player_screen.ui.player_utils.play_back.PlayBackControl
import com.project.playlistmaker.player_screen.ui.player_utils.update_duration_view.UpdateDurationTaskImpl
import com.project.playlistmaker.player_screen.ui.view_model.ActivityPlayerViewModel
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.use_cases.utilities.DataFormat

class ActivityPlayer : ComponentActivity() {

    companion object {
        const val TRACK_DTO_DATA = "track_dto_data"
        const val SEPARATE_DATE_STR_YEAR = 4
    }

    //Views
    private lateinit var backBtn: ImageButton
    private lateinit var addToPlaylistBtn: ImageButton
    private lateinit var playPauseBtn: ImageButton
    private lateinit var addToFavBtn: ImageButton
    private lateinit var albumCover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var durationTotalTv: TextView
    private lateinit var albumTv: TextView
    private lateinit var yearTv: TextView
    private lateinit var genreTv: TextView
    private lateinit var countryTv: TextView
    private lateinit var currentDuration: TextView

    //ViewModel
    private lateinit var activityPlayerViewModel: ActivityPlayerViewModel

    //Other
    private var cornerSize: Int = 0
    private lateinit var track: Track
    private val dataFormat = DataFormat()
    private lateinit var playBackControl: PlayBackControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        bindViews()

        track = intent.getSerializableExtra(TRACK_DTO_DATA) as Track

        activityPlayerViewModel =
            ViewModelProvider(
                this,
                ActivityPlayerViewModel.getViewModelFactory()
            )[ActivityPlayerViewModel::class.java]


        initPlayBackControl()

        fillContent()

        backBtn.setOnClickListener {
            onBackPressed()
        }

        playPauseBtn.setOnClickListener {
            playBackControl.playbackControl()
        }

        activityPlayerViewModel.observePlayerState().observe(this) {
            PlayerState.STATE_DEFAULT
        }
    }

    override fun onDestroy() {
        activityPlayerViewModel.releasePlayer()
        super.onDestroy()
    }

    override fun onPause() {
        activityPlayerViewModel.pausePlayer()
        super.onPause()
    }

    private fun fillContent() {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(cornerSize))
            .placeholder(R.drawable.placeholder)
            .into(albumCover)
        trackName.text = track.trackName
        artistName.text = track.artistName
        durationTotalTv.text = dataFormat.convertTimeToMnSs(track.trackTimeMillis)

        albumTv.text = track.collectionName
        yearTv.text = track.releaseDate.substring(0, SEPARATE_DATE_STR_YEAR)
        genreTv.text = track.primaryGenreName
        countryTv.text = track.country
    }

    private fun initPlayBackControl() {
        playBackControl = PlayBackControl(
            activityPlayerViewModel,
            UpdateDurationTaskImpl(
                activityPlayerViewModel,
                currentDuration,
                this
            ),
            playPauseBtn,
            track.previewUrl
        )
    }

    //Binding
    private fun bindViews() {
        backBtn = findViewById(R.id.back_btn_player)
        addToPlaylistBtn = findViewById(R.id.add_to_playlist_btn)
        playPauseBtn = findViewById(R.id.play_pause_btn)
        addToFavBtn = findViewById(R.id.add_to_fav_btn)
        albumCover = findViewById(R.id.album_cover_iv)
        trackName = findViewById(R.id.track_name_in_player_tv)
        artistName = findViewById(R.id.artist_name_tv)
        durationTotalTv = findViewById(R.id.duration_total_tv)
        albumTv = findViewById(R.id.album_in_table_tv)
        yearTv = findViewById(R.id.year_in_table_tv)
        genreTv = findViewById(R.id.genre_in_table_tv)
        countryTv = findViewById(R.id.country_in_table_tv)
        currentDuration = findViewById(R.id.track_duration_current)
        cornerSize = this.resources.getDimensionPixelSize(R.dimen.cover_corner_size)
    }
}